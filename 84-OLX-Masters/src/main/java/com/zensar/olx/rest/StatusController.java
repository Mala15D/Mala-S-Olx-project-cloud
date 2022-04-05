package com.zensar.olx.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.zensar.olx.bean.AdvertisementStatus;
import com.zensar.olx.service.AdvertiseStatusService;

@RestController
public class StatusController {

	@Autowired
	AdvertiseStatusService service;

	@PostMapping("/advertise/addadvertise")
	public AdvertisementStatus addAdvertisementStatus(@RequestBody AdvertisementStatus advertisementStatus) {
		return this.service.addAdvertisementStatus(advertisementStatus);
	}

	@GetMapping("/advertise/getAllStatus")
	public List<AdvertisementStatus> getAllAdvertisementStatus() {
		return this.service.getAllStatus();
	}

	@GetMapping("/advertise/status/{id}")
	public AdvertisementStatus findAdvertisemetStatusById(@PathVariable(name = "id") int id) {
		return this.service.findAdvertisementStatus(id);
	}
}
