package de.phigroup.websocket.monitor.jpa.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import de.phigroup.websocket.monitor.jpa.entity.Host;

/**
 * LI 20161215: http://stackoverflow.com/questions/31724994/spring-data-rest-and-cors
 * CORS for Spring Data REST is not supported before v2.6! 
 * 
 * @author eztup
 *
 */
@RepositoryRestResource(collectionResourceRel = "hosts", path = "hosts")
//@CrossOrigin(origins = {"http://localhost:9090", "http://wu17035962-lsy.ads.dlh.de:9090"})
//@CrossOrigin(origins = "*")
public interface HostRepository extends PagingAndSortingRepository<Host, Long> {

	List<Host> findByName(@Param("name") String name);

	List<Host> findByIp(@Param("ip") String ip);

}