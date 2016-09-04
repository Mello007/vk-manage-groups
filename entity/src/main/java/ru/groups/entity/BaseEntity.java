package ru.groups.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity {
    @javax.persistence.Id @GeneratedValue(strategy = GenerationType.SEQUENCE) @Column protected Long Id;
}