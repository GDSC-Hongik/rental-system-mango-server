package mango.rentalsystem.domain.rental.dao;

import org.springframework.data.repository.CrudRepository;

import mango.rentalsystem.domain.rental.domain.Rental;

public interface RentalRepository extends CrudRepository<Rental, Long> {
}
