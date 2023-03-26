/*
 * 
 * Copyright 2014 Jules White
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package org.magnum.mobilecloud.video;

import java.io.IOException;
import java.security.Principal;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.magnum.mobilecloud.video.client.VideoSvcApi;
import org.magnum.mobilecloud.video.repository.Video;
import org.magnum.mobilecloud.video.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VideoController{
	
	/**
	 * You will need to create one or more Spring controllers to fulfill the
	 * requirements of the assignment. If you use this file, please rename it
	 * to something other than "AnEmptyController"
	 * 
	 * 
		 ________  ________  ________  ________          ___       ___  ___  ________  ___  __       
		|\   ____\|\   __  \|\   __  \|\   ___ \        |\  \     |\  \|\  \|\   ____\|\  \|\  \     
		\ \  \___|\ \  \|\  \ \  \|\  \ \  \_|\ \       \ \  \    \ \  \\\  \ \  \___|\ \  \/  /|_   
		 \ \  \  __\ \  \\\  \ \  \\\  \ \  \ \\ \       \ \  \    \ \  \\\  \ \  \    \ \   ___  \  
		  \ \  \|\  \ \  \\\  \ \  \\\  \ \  \_\\ \       \ \  \____\ \  \\\  \ \  \____\ \  \\ \  \ 
		   \ \_______\ \_______\ \_______\ \_______\       \ \_______\ \_______\ \_______\ \__\\ \__\
		    \|_______|\|_______|\|_______|\|_______|        \|_______|\|_______|\|_______|\|__| \|__|
                                                                                                                                                                                                                                                                        
	 * 
	 */
	
	@Autowired
	private VideoRepository videoRepository;	

	@GetMapping(VideoSvcApi.VIDEO_SVC_PATH)
	public Collection<Video> getVideoList() {
		return (Collection<Video>) videoRepository.findAll();
	}

	@PostMapping(VideoSvcApi.VIDEO_SVC_PATH)
	public Video addVideo(@RequestBody Video v) {
		return videoRepository.save(v);
	}

	@GetMapping(VideoSvcApi.VIDEO_SVC_PATH + "/{id}")
	public Video getVideoById(@PathVariable("id") long id) {
		return videoRepository.findById(id);
	}
	
	@PostMapping(VideoSvcApi.VIDEO_SVC_PATH + "/{id}/like")
	public void likeVideo(@PathVariable("id") long id,
						  Principal user,
						  HttpServletResponse resp) throws IOException {
		Video v = videoRepository.findById(id);
		if(v == null){
			resp.sendError(404);
			return;
		}
		String user_name = user.getName();
		if (v.UserhasLiked(user_name)) {
			resp.sendError(400);
			return;
		}
		v.addLikedBy(user_name);
		v.setLikes(v.getLikes() + 1);
		videoRepository.save(v);
	}

	@PostMapping(VideoSvcApi.VIDEO_SVC_PATH + "/{id}/unlike")
	public void unlikeVideo(@PathVariable("id") long id,
							Principal user,
							HttpServletResponse resp) throws IOException {
		Video v = videoRepository.findById(id);
		if(v == null){
			resp.sendError(404);
			return;
		}
		String user_name = user.getName();
		if (!v.UserhasLiked(user_name)) {
			resp.sendError(400);
			return;
		}
		v.removeLikedBy(user_name);
		v.setLikes(v.getLikes() - 1);
		videoRepository.save(v);
	}

	@GetMapping(VideoSvcApi.VIDEO_TITLE_SEARCH_PATH)
	public Collection<Video> findByTitle(@RequestParam(VideoSvcApi.TITLE_PARAMETER) String title) {
		return videoRepository.findByName(title);
	}

	@GetMapping(VideoSvcApi.VIDEO_DURATION_SEARCH_PATH)
	public Collection<Video> findByDurationLessThen(@RequestParam(VideoSvcApi.DURATION_PARAMETER) long duration) {
		return videoRepository.findByDurationLessThan(duration);
	}

//	@Override
//	public Void likeVideo(long id) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Void unlikeVideo(long id) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Collection<Video> findByDurationLessThan(long duration) {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
