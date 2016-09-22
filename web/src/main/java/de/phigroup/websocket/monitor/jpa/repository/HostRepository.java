package de.phigroup.websocket.monitor.jpa.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import de.phigroup.websocket.monitor.jpa.entity.Host;

/**
 * 
 * @author eztup
 *
 */
@RepositoryRestResource(collectionResourceRel = "hosts", path = "hosts")
public interface HostRepository extends PagingAndSortingRepository<Host, Long> {

	List<Host> findByName(@Param("name") String name);

	List<Host> findByIp(@Param("ip") String ip);

}