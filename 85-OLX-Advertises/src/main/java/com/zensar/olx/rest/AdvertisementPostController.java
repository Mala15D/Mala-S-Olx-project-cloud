package com.zensar.olx.rest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.zensar.olx.bean.AdvertisementPost;
import com.zensar.olx.bean.AdvertisementStatus;
import com.zensar.olx.bean.Category;
import com.zensar.olx.bean.FilterCriteriaRequest;
import com.zensar.olx.bean.LoginUser;
import com.zensar.olx.bean.NewAdvertisementPostRequest;
import com.zensar.olx.bean.NewAdvertisementPostResponse;
import com.zensar.olx.bean.OlxUser;
import com.zensar.olx.service.AdvertisementPostService;

@RestController
public class AdvertisementPostController {

	@Autowired
	AdvertisementPostService service;

	// 8th RestEndPoint
	@PostMapping("/advertise/{un}")
	public NewAdvertisementPostResponse add(@RequestBody NewAdvertisementPostRequest request,
			@PathVariable(name = "un") String userName) {
		AdvertisementPost post = new AdvertisementPost();
		post.setTitle(request.getTitle());
		post.setPrice(request.getPrice());
		post.setDescription(request.getDescription());

		int categoryId = request.getCategoryId();

		RestTemplate restTemplate = new RestTemplate();
		Category category;
		String url = "http://localhost:9052/advertise/getCategory/" + categoryId;
		category = restTemplate.getForObject(url, Category.class);
		post.setCategory(category);

		url = "http://localhost:9051/user/find/" + userName;
		OlxUser olxUser = restTemplate.getForObject(url, OlxUser.class);
		post.setOlxUser(olxUser);

		AdvertisementStatus advertisementStatus = new AdvertisementStatus(1, "OPEN");
		post.setAdvertisementStatus(advertisementStatus);

		AdvertisementPost advertisementPost = this.service.addAdvertisement(post);// entity saved to db

		NewAdvertisementPostResponse response = new NewAdvertisementPostResponse();
		response.setId(advertisementPost.getId());
		response.setTitle(advertisementPost.getTitle());
		response.setPrice(advertisementPost.getPrice());
		response.setCategory(advertisementPost.getCategory().getName());
		response.setDescription(advertisementPost.getDescription());
		response.setUserName(advertisementPost.getOlxUser().getUserName());
		response.setCreatedDate(advertisementPost.getCreatedDate());
		response.setModifiedDate(advertisementPost.getModifiedDate());
		response.setStatus(advertisementPost.getAdvertisementStatus().getStatus());
		return response;
	}

	// 9th RestEndPoint
	@PutMapping("/advertise/{aid}/{userName}")
	public NewAdvertisementPostResponse f2(@RequestBody NewAdvertisementPostRequest request,
			@PathVariable(name = "aid") int id, @PathVariable(name = "userName") String userName) {

		AdvertisementPost post = this.service.getAdvertisementById(id);

		post.setTitle(request.getTitle());
		post.setDescription(request.getDescription());
		post.setPrice(request.getPrice());

		RestTemplate restTemplate = new RestTemplate();
		Category category;
		String url = "http://localhost:9052/advertise/getCategory/" + request.getCategoryId();
		category = restTemplate.getForObject(url, Category.class);
		post.setCategory(category);

		url = "http://localhost:9051/user/find/" + userName;
		OlxUser olxUser = restTemplate.getForObject(url, OlxUser.class);
		post.setOlxUser(olxUser);

		url = "http://localhost:9052/advertise/status/" + request.getStatusId();
		AdvertisementStatus advertisementStatus;
		advertisementStatus = restTemplate.getForObject(url, AdvertisementStatus.class);
		post.setAdvertisementStatus(advertisementStatus);

		AdvertisementPost advertisementPost = this.service.updateAdvertisement(post); // writing to db

		NewAdvertisementPostResponse postResponse;
		postResponse = new NewAdvertisementPostResponse();

		postResponse.setId(advertisementPost.getId());
		postResponse.setTitle(advertisementPost.getTitle());
		postResponse.setDescription(advertisementPost.getDescription());
		postResponse.setPrice(advertisementPost.getPrice());
		postResponse.setUserName(advertisementPost.getOlxUser().getUserName());
		postResponse.setCategory(advertisementPost.getCategory().getName());
		postResponse.setCreatedDate(advertisementPost.getCreatedDate());
		postResponse.setModifiedDate(advertisementPost.getModifiedDate());
		postResponse.setStatus(advertisementPost.getAdvertisementStatus().getStatus());

		return postResponse;

	}

	// 10th RestEndPoint
	@GetMapping("/user/advertise/{userName}")
	public List<NewAdvertisementPostResponse> f3(@PathVariable(name = "userName") String userName) {
		List<AdvertisementPost> allPosts = this.service.getAllAdverisements();

		RestTemplate restTemplate = new RestTemplate();
		List<AdvertisementPost> filterList = new ArrayList<>();

		String url = "http://localhost:9051/user/find/" + userName;
		OlxUser olxUser = restTemplate.getForObject(url, OlxUser.class);

		for (AdvertisementPost post : allPosts) {
			Category category;
			url = "http://localhost:9052/advertise/getCategory/" + post.getCategory().getId();
			category = restTemplate.getForObject(url, Category.class);
			post.setCategory(category);

			url = "http://localhost:9052/advertise/status/" + post.getAdvertisementStatus().getId();
			AdvertisementStatus advertisementStatus;
			advertisementStatus = restTemplate.getForObject(url, AdvertisementStatus.class);
			post.setAdvertisementStatus(advertisementStatus);

			if (olxUser.getOlxUserId() == post.getOlxUser().getOlxUserId()) {
				post.setOlxUser(olxUser);
				filterList.add(post);
			}
		}

		List<NewAdvertisementPostResponse> responseList = new ArrayList<>();
		for (AdvertisementPost advertisementPost : filterList) {
			NewAdvertisementPostResponse postRespone = new NewAdvertisementPostResponse();
			postRespone.setId(advertisementPost.getId());
			postRespone.setTitle(advertisementPost.getTitle());
			postRespone.setDescription(advertisementPost.getDescription());
			postRespone.setPrice(advertisementPost.getPrice());
			postRespone.setUserName(advertisementPost.getOlxUser().getUserName());
			postRespone.setCategory(advertisementPost.getCategory().getName());
			postRespone.setCreatedDate(advertisementPost.getCreatedDate());
			postRespone.setModifiedDate(advertisementPost.getModifiedDate());
			postRespone.setStatus(advertisementPost.getAdvertisementStatus().getStatus());

			responseList.add(postRespone);
		}
		return responseList;

	}

	// 11th RestEndPoint
	@GetMapping("/user/advertise/{un}/{aid}")
	public NewAdvertisementPostResponse f4(@PathVariable(name = "un") String userName,
			@PathVariable(name = "aid") int id) {
		AdvertisementPost advertisementPost = this.service.getAdvertisementById(id);

		RestTemplate restTemplate = new RestTemplate();
		List<AdvertisementPost> filterList = new ArrayList<>();

		String url = "http://localhost:9051/user/find/" + userName;
		OlxUser olxUser = restTemplate.getForObject(url, OlxUser.class);

		Category category;
		url = "http://localhost:9052/advertise/getCategory/" + advertisementPost.getCategory().getId();
		category = restTemplate.getForObject(url, Category.class);
		advertisementPost.setCategory(category);

		url = "http://localhost:9052/advertise/status/" + advertisementPost.getAdvertisementStatus().getId();
		AdvertisementStatus advertisementStatus;
		advertisementStatus = restTemplate.getForObject(url, AdvertisementStatus.class);
		advertisementPost.setAdvertisementStatus(advertisementStatus);

		if (olxUser.getOlxUserId() == advertisementPost.getOlxUser().getOlxUserId()) {
			advertisementPost.setOlxUser(olxUser);
			filterList.add(advertisementPost);
		}

		NewAdvertisementPostResponse postResponse = new NewAdvertisementPostResponse();

		postResponse.setId(advertisementPost.getId());
		postResponse.setTitle(advertisementPost.getTitle());
		postResponse.setDescription(advertisementPost.getDescription());
		postResponse.setPrice(advertisementPost.getPrice());
		postResponse.setUserName(advertisementPost.getOlxUser().getUserName());
		postResponse.setCategory(advertisementPost.getCategory().getName());
		postResponse.setCreatedDate(advertisementPost.getCreatedDate());
		postResponse.setModifiedDate(advertisementPost.getModifiedDate());
		postResponse.setStatus(advertisementPost.getAdvertisementStatus().getStatus());
		return postResponse;

	}

	// 12th RestEndPoint
	@DeleteMapping("/user/advertise/{un}/{aid}")
	public boolean f6(@PathVariable(name = "un") String userName, @PathVariable(name = "aid") int id) {

		List<AdvertisementPost> advertisementPosts = this.service.getAllAdverisements();
		RestTemplate restTemplate = new RestTemplate();

		String url = "http://localhost:9051/user/find/" + userName;
		OlxUser olxUser = restTemplate.getForObject(url, OlxUser.class);

		boolean result = false;
		for (AdvertisementPost post : advertisementPosts) {
			if (post.getId() == id) {
				if (olxUser.getUserName().equals(userName)) {
					result = this.service.deleteadvertisementPost(post);

				}
			}

		}
		return result;
	}

	// 13th RestEndPoint
	@GetMapping("/advertise/search/{filter}")
	public List<NewAdvertisementPostResponse> filterAdvertisements(@RequestBody FilterCriteriaRequest criteriaRequest) {

		LocalDate dateFrom = criteriaRequest.getFromDate();
		LocalDate dateTo = criteriaRequest.getToDate();

		List<AdvertisementPost> allAdvertisementPosts = this.service.getAllAdverisements();
		List<NewAdvertisementPostResponse> responseList = new LinkedList<>();

		for (AdvertisementPost advertisementPost : allAdvertisementPosts) {
			NewAdvertisementPostResponse response = new NewAdvertisementPostResponse();
			RestTemplate restTemplate = new RestTemplate();

			Category category = advertisementPost.getCategory();
			String url = "http://localhost:9052/advertise/getCategory/" + category.getId();
			category = restTemplate.getForObject(url, Category.class);
			response.setCategory(category.getName());
			response.setDescription(category.getDescription());
			response.setId(advertisementPost.getId());
			response.setTitle(advertisementPost.getTitle());
			response.setPrice(advertisementPost.getPrice());
			response.setCreatedDate(advertisementPost.getCreatedDate());
			response.setModifiedDate(advertisementPost.getModifiedDate());

			OlxUser olxUser = advertisementPost.getOlxUser();
			url = "http://localhost:9051/user/" + olxUser.getOlxUserId();
			olxUser = restTemplate.getForObject(url, OlxUser.class);
			response.setUserName(olxUser.getUserName());

			AdvertisementStatus advertisementStatus = advertisementPost.getAdvertisementStatus();
			// System.out.println(advertisementStatus.getId());
			url = "http://localhost:9052/advertise/status/" + advertisementStatus.getId();
			advertisementStatus = restTemplate.getForObject(url, AdvertisementStatus.class);
			response.setStatus(advertisementStatus.getStatus());

			responseList.add(response);
		}

		return responseList;
	}

	// 14th RestEndPoint
	@GetMapping("/advertise/{search}")
	public List<NewAdvertisementPostResponse> f7(@PathVariable(name = "search") String searchText) {
		List<AdvertisementPost> allPost = this.service.getAllAdverisements();
		System.out.println(allPost);
		RestTemplate restTemplate = new RestTemplate();
		for (AdvertisementPost advertisementPost : allPost) {
			String url = "http://localhost:9051/user/" + advertisementPost.getOlxUser().getOlxUserId();
			OlxUser olxUser = restTemplate.getForObject(url, OlxUser.class);
			advertisementPost.setOlxUser(olxUser);
			Category category;
			url = "http://localhost:9052/advertise/getCategory/" + advertisementPost.getCategory().getId();
			category = restTemplate.getForObject(url, Category.class);
			advertisementPost.setCategory(category);
			url = "http://localhost:9052/advertise/status/" + advertisementPost.getAdvertisementStatus().getId();
			AdvertisementStatus advertisementStatus;
			advertisementStatus = restTemplate.getForObject(url, AdvertisementStatus.class);
			advertisementPost.setAdvertisementStatus(advertisementStatus);
		}
		List<AdvertisementPost> filterPosts = new ArrayList<>();
		for (AdvertisementPost advertisementPost : allPost) {
			if ((advertisementPost.getCategory().getName().toLowerCase().contains(searchText.toLowerCase()))
					|| (advertisementPost.getTitle().toLowerCase().contains(searchText.toLowerCase()))
					|| (advertisementPost.getDescription().toLowerCase().contains(searchText.toLowerCase()))
					|| (advertisementPost.getAdvertisementStatus().getStatus().toLowerCase()
							.contains(searchText.toLowerCase()))) {
				filterPosts.add(advertisementPost);
			}
		}
		List<NewAdvertisementPostResponse> responce = new ArrayList<>();
		for (AdvertisementPost advertisementPost : filterPosts) {
			NewAdvertisementPostResponse postRespone = new NewAdvertisementPostResponse();
			postRespone.setId(advertisementPost.getId());
			postRespone.setTitle(advertisementPost.getTitle());
			postRespone.setUserName(advertisementPost.getOlxUser().getUserName());
			postRespone.setDescription(advertisementPost.getDescription());
			postRespone.setPrice(advertisementPost.getPrice());
			postRespone.setCategory(advertisementPost.getCategory().getName());
			postRespone.setCreatedDate(advertisementPost.getCreatedDate());
			postRespone.setModifiedDate(advertisementPost.getModifiedDate());
			postRespone.setStatus(advertisementPost.getAdvertisementStatus().getStatus());
			responce.add(postRespone);
		}
		return responce;
	}

	// 15th RestEndPoint
	@GetMapping("/advertise/{aid}")
	public NewAdvertisementPostResponse f8(@PathVariable(name = "aid") int id) {
		AdvertisementPost advertisementPost = this.service.getAdvertisementById(id);
		NewAdvertisementPostResponse postResponse = new NewAdvertisementPostResponse();

		postResponse.setId(advertisementPost.getId());
		postResponse.setTitle(advertisementPost.getTitle());
		postResponse.setDescription(advertisementPost.getDescription());
		postResponse.setPrice(advertisementPost.getPrice());
		postResponse.setCategory(advertisementPost.getCategory().getName());
		postResponse.setCreatedDate(advertisementPost.getCreatedDate());
		postResponse.setModifiedDate(advertisementPost.getModifiedDate());
		postResponse.setStatus(advertisementPost.getAdvertisementStatus().getStatus());
		return postResponse;
	}

}
