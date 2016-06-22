package com.procergs.rsp.post;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.procergs.rsp.list.ListService;
import com.procergs.rsp.opengraph.OpenGraph;
import com.procergs.rsp.opengraph.OpenGraphService;
import com.procergs.rsp.opengraph.ed.OpenGraphED;
import com.procergs.rsp.post.ed.*;
import org.apache.commons.io.IOUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.pt.PortugueseAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRefBuilder;
import org.apache.lucene.util.NumericUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import com.procergs.rsp.image.ImageService;
import com.procergs.rsp.image.ed.ImageED;
import com.procergs.rsp.list.ed.ListED;
import com.procergs.rsp.user.ed.UserEd;
import com.procergs.rsp.user.ed.UserRequestED;
import com.procergs.rsp.util.RSPUtil;

@Stateless
@Named
@Path("post")
public class PostService {

	public static final String POST_INSERT_LIST_NAME = "ln";
	public static final String POST_INSERT_LIST_ID = "l";

	@PersistenceContext(unitName = "RSP_PU")
	EntityManager em;

	PostBD postBD;

	Directory directory;

	@EJB
	ImageService imageService;

	@EJB
	ListService listService;

  @EJB
  OpenGraphService openGraphService;

	@PostConstruct
	public void init() {
		postBD = new PostBD(em);
		try {
			// directory = FSDirectory.open(Paths.get(new
			// URI("file:///"+System.getProperty("java.io.tmpdir")+File.separator
			// + "rsp"+File.separator )));
			directory = FSDirectory.open(Paths.get(System.getProperty("rsp.lucene.path")));
			// directory.create();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// catch (URISyntaxException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional
	public boolean post(MultipartFormDataInput input, @Context HttpServletRequest httpRequest) {

		try {
			Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
			PostED postED = new PostED();
			postED.setTexto(RSPUtil.getParameterValue(input, "t"));
			postED.setUserEd(((UserRequestED) httpRequest.getAttribute(UserRequestED.ATRIBUTO_REQ_USER)).getUserEd());
			postED.setData(Calendar.getInstance());

			if (uploadForm.containsKey(POST_INSERT_LIST_ID)) {
				Long idList = uploadForm.get(POST_INSERT_LIST_ID).get(0).getBody(Long.class, null);
				if (idList != null) {
					postED.setLists(Arrays.asList(new ListED(idList)));
				}
			} else if(uploadForm.containsKey(POST_INSERT_LIST_NAME)) {
				List<InputPart> listsNames = uploadForm.get(POST_INSERT_LIST_NAME);
				List<ListED> lists = new ArrayList<>();
				for (InputPart listsNamePart: listsNames){
					String listName = listsNamePart.getBodyAsString();
					if(listName == null || listName.isEmpty()){
						continue;
					}
					ListED listed = listService.findByName(listName);
					if(listed == null){
						listed = new ListED(listName);
						listService.insert(listed);
					}
					lists.add(listed);
				}
				postED.setLists(lists);
			}
			
			if (uploadForm.containsKey("pp")) {
				Long idParentPost = uploadForm.get("pp").get(0).getBody(Long.class, null);
				if (idParentPost != null) {
					postED.setParent(new PostED(idParentPost));
				}
			}

			insertOGP(postED);

			postBD.insert(postED);

			List<ImageED> limages = RSPUtil.getImages(input, "pi");
			for (ImageED imageED : limages) {
				imageED.setPostED(postED);
				imageService.insert(imageED);
			}

			indexPost(postED);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	private void insertOGP(PostED postED) {
		String [] parts = postED.getTexto().split("\\s+");
		for( String item : parts ) {
			try {
				URL url = new URL(item);
				OpenGraph og = new OpenGraph(item, true);
				OpenGraphED openGraphED = new OpenGraphED();
				openGraphED.setTitle(og.getContent("title"));
				openGraphED.setUrl(og.getContent("url") == null ? og.getOriginalUrl(): og.getContent("url"));
				openGraphED.setDescription(og.getContent("description"));
				openGraphED.setImage(og.getContent("image"));

				OpenGraphED openGraphEDInBase = openGraphService.findByUrl(openGraphED);
				if(openGraphEDInBase == null){
					openGraphService.insert(openGraphED);
				} else {
					openGraphED = openGraphEDInBase;
				}

				postED.setOpenGraphED(openGraphED);
				return;
				// If possible then replace with anchor...
				//System.out.print("<a href=\"" + url + "\">" + url + "</a> ");
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (Exception e){
        		e.printStackTrace();
			}
		}
	}

	private void indexPost(PostED postED) {
		Document d = new Document();

		d.add(new TextField("text", postED.getTexto(), Field.Store.YES ));
		d.add(new SortedNumericDocValuesField("id", postED.getIdPost() ));

		try {
			IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(new PortugueseAnalyzer()));
			writer.addDocument(d);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * list timeline
	 * 
	 * @param httpRequest
	 * @return
	 */
	@GET
	@Produces( MediaType.APPLICATION_JSON)
	public Collection<PostResultED> list(@QueryParam("l") Long idList, @QueryParam("u") Long idUser,
										 @QueryParam("lp") Long idLastPost, @QueryParam("fp") Long idFirstPost, @Context HttpServletRequest httpRequest) {
		UserEd user = ((UserRequestED) httpRequest.getAttribute(UserRequestED.ATRIBUTO_REQ_USER)).getUserEd();

		Collection<PostED> list = null;
		if (idList != null) {
			list =  postBD.listPostList(idList, idLastPost, idFirstPost);
		} else if (idUser != null) {
			list =  postBD.listPostUser(idUser, idLastPost, idFirstPost);
		} else {
			list = postBD.list(user, idLastPost, idFirstPost);
		}


		return list.stream().map(postED -> populatePostResult(postED)).collect(Collectors.toList());
	}

	private PostResultED populatePostResult(PostED postED) {
		PostResultEDBuilder builder = new PostResultEDBuilder(postED,
															postBD.listReplies(postED).stream().map(postEDChild -> populatePostResult(postEDChild) ),
															imageService.listImages(postED),
															postBD.listLikes(postED));
		return builder.build();
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/{idpost}")
	public PostResultED load(@PathParam("idpost") Long idPost, @Context HttpServletRequest httpRequest) {
		PostED posted = postBD.load(idPost);
		return populatePostResult(posted);
	}
	

	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/l/{idpost}")
	public PostResultED like(@PathParam("idpost") Long idPost, @Context HttpServletRequest httpRequest) {
		LikeED likeED = new LikeED();
		likeED.setDate(Calendar.getInstance());
		UserEd user = ((UserRequestED) httpRequest.getAttribute(UserRequestED.ATRIBUTO_REQ_USER)).getUserEd();
		likeED.setUserEd(user);
		likeED.setPostED(new PostED(idPost));
		postBD.insertLike(likeED);
		em.flush();
		return load(idPost, httpRequest);
	}
	
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/dl/{idpost}")
	public PostResultED dislike(@PathParam("idpost") Long idPost, @Context HttpServletRequest httpRequest) {
		LikeED likeED = new LikeED();
		likeED.setDate(Calendar.getInstance());
		UserEd user = ((UserRequestED) httpRequest.getAttribute(UserRequestED.ATRIBUTO_REQ_USER)).getUserEd();
		likeED.setUserEd(user);
		likeED.setPostED(new PostED(idPost));
		postBD.deleteLike(likeED);
		em.flush();
		return load(idPost, httpRequest);
	}


	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/d/{idpost}")
	public void delete(@PathParam("idpost") Long idPost, @Context HttpServletRequest httpRequest) {
		UserEd user = ((UserRequestED) httpRequest.getAttribute(UserRequestED.ATRIBUTO_REQ_USER)).getUserEd();
		PostED postED = postBD.load(idPost);
		if(postED.getUserEd().equals(user)){
			deletePost(postED);
		}



	}
	private void deletePost(PostED postED){
		Collection<PostED> replies = postBD.listReplies(postED);
		replies.forEach(postED1 -> deletePost(postED1));

        postBD.delete(postED);
        try {
            IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(new PortugueseAnalyzer()));
            BytesRefBuilder ref = new BytesRefBuilder();
            NumericUtils.longToPrefixCoded( postED.getIdPost(), 0, ref );
            writer.deleteDocuments(new Term("id", ref));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

	}

    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Path("/s")
    public PostSearchResultED search(@FormParam("st") String searchTerm, @Context HttpServletRequest httpRequest) {
        PostSearchResultED postSearchResult = new PostSearchResultED();

        try{
            IndexReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);
            Analyzer analyzer = new PortugueseAnalyzer();
			QueryParser parser = new QueryParser("text", analyzer);
			org.apache.lucene.search.Query query = parser.parse(searchTerm);

			Sort sort = new Sort();
			sort.setSort(new SortedNumericSortField("id", SortField.Type.LONG, true));
            TopDocs results = searcher.search(query, 10, sort);
            ScoreDoc[] hits = results.scoreDocs;

            int numTotalHits = results.totalHits;
            System.out.println(numTotalHits + " total matching documents");

            int start = 0;
            int end = Math.min(numTotalHits, 20);

            Set<PostResultED> listPosts = new HashSet<>();
            postSearchResult.setPosts(listPosts);

            for (int i = start; i < end; i++) {
                System.out.println("doc="+hits[i].doc+" score="+hits[i].score);

                Document doc = searcher.doc(hits[i].doc);
                String idPost = doc.get("id");
                if (idPost != null) {
					PostResultED postED = loadParent(Long.valueOf(idPost), httpRequest);
					if(postED != null){
						listPosts.add(postED);
					}
                    System.out.println((i+1) + ". " + idPost);
                    String title = doc.get("text");
                    if (title != null) {
                        System.out.println("   Title: " + doc.get("text"));
                    }
                } else {
                    System.out.println((i+1) + ". " + "No path for this document");
                }

            }

            reader.close();

        } catch (IOException e){
            e.printStackTrace();
        } catch (ParseException e) {
					e.printStackTrace();
		} finally {

        }
        return postSearchResult;
    }

	private PostResultED loadParent(Long idPost, HttpServletRequest httpRequest) {
		PostED postED = postBD.load(idPost);
		if(postED == null){
			return  null;
		}

		while (postED.getParent() != null){
			postED = postED.getParent();
		}

		return populatePostResult(postED);
	}


}
