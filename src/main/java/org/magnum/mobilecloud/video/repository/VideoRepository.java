package org.magnum.mobilecloud.video.repository;

import java.util.Collection;

import org.magnum.mobilecloud.video.client.VideoSvcApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

//@RepositoryRestResource(path = VideoSvcApi.VIDEO_SVC_PATH)
public interface VideoRepository extends CrudRepository<Video, Long> {
	public Video findById(long id);
	
    public Collection<Video> findByName(@Param(VideoSvcApi.TITLE_PARAMETER) String title);

    public Collection<Video> findByDurationLessThan(@Param(VideoSvcApi.DURATION_PARAMETER) long duration); 
}
