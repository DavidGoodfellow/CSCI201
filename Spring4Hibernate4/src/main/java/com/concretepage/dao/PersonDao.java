package com.concretepage.dao;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;

import com.concretepage.entity.Person;

@Transactional
public class PersonDao implements IPersonDao {
	@Autowired
	private HibernateTemplate  hibernateTemplate;
	public void savePerson() {
		Person person = new Person();
		person.setId(1);
		person.setName("Ram");
		hibernateTemplate.save(person);
	}
}
