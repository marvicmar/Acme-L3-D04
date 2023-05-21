
package acme.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import acme.entities.banner.Banner;
import acme.services.BannerService;

@ControllerAdvice
public class BannerAdvisor {

	// Internal state ---------------------------------------------------------
	@Autowired
	private BannerService repository;

	// Methods -----------------------------------------------------------------
	@ModelAttribute("banner")
	public Banner getBanner() {
		Banner result;

		result = this.repository.findRandomBanner();

		return result;
	}
}
