
package acme.services;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import acme.entities.banner.Banner;
import acme.framework.helpers.MessageHelper;
import acme.repositories.BannerRepository;

@Service
public class BannerService {

	// Internal state ---------------------------------------------------------
	@Autowired
	private BannerRepository repository;

	// Methods -----------------------------------------------------------------
	public Banner findRandomBanner() {
		int bannerCount;
		int bannerIndex;
		Banner defaultBanner;
		PageRequest pageRequest;
		String defaultSlogan;
		Banner result;

		defaultBanner = new Banner();
		defaultSlogan = MessageHelper.getMessage("master.banner.alt");
		defaultBanner.setSlogan(defaultSlogan);
		defaultBanner.setPicture("images/banner.png");
		defaultBanner.setLink("https://www.us.es/");

		bannerCount = this.repository.countBanners();
		if (bannerCount == 0)
			return defaultBanner;

		bannerIndex = ThreadLocalRandom.current().nextInt(bannerCount);
		pageRequest = PageRequest.of(bannerIndex, 1);
		result = this.repository.findManyBanners(pageRequest).stream().findFirst().orElse(defaultBanner);

		return result;
	}
}
