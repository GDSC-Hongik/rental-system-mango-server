package mango.rentalsystem.domain;


import jakarta.persistence.*;

import java.util.LinkedList;
import java.util.Date;
import java.util.List;

@Entity
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String studentId;

    private String password;

    private String name;

    private MemberRole role;

    @ManyToOne
    @JoinColumn(name = "department")
    private Department department;

    private String phone;

    private String pictureUrl;

    private List<Rental> rentalList = new LinkedList<>();

    private boolean absenceStatus;

    private Date rentalUnavailableDate;



}
