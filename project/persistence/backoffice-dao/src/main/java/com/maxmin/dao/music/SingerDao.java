package com.maxmin.dao.music;

import java.util.Optional;
import java.util.Set;

import com.maxmin.domain.model.music.Singer;

public interface SingerDao extends CoreDao {

	Set<Singer> findAll();

	Set<Singer> findByFirstName(String firstName);

	Optional<String> findNameById(Long id);

	Optional<String> findLastNameById(Long id);

	Optional<String> findFirstNameById(Long id);

	void insert(Singer singer);

	void update(Singer singer);

	void delete(Long singerId);

	Set<Singer> findAllWithAlbums();

	void insertWithAlbum(Singer singer);
}
