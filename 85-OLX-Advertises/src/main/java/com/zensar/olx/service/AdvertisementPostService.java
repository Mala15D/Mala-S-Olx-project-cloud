package com.zensar.olx.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.zensar.olx.bean.AdvertisementPost;
import com.zensar.olx.db.AdvertisementPostDAO;

@Service
public class AdvertisementPostService {

	@Autowired
	AdvertisementPostDAO dao;

	public AdvertisementPost addAdvertisement(AdvertisementPost advertisementPost) {
		return this.dao.save(advertisementPost);
	}

	public AdvertisementPost updateAdvertisement(AdvertisementPost advertisementPost) {
		return this.dao.save(advertisementPost);
	}
	

	public List<AdvertisementPost> getAllAdverisements() {
		return this.dao.findAll();
	}

	public boolean deleteadvertisementPost(AdvertisementPost advertisementPost) {
		boolean result = false;
		try {
			this.dao.delete(advertisementPost);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	public AdvertisementPost getAdvertisementById(int id) {
		Optional<AdvertisementPost> optional;
		optional = dao.findById(id);
		if (optional.isPresent())
			return optional.get();
		else
			return null;

	}
}
