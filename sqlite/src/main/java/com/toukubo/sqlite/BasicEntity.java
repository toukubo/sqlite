package com.toukubo.sqlite;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Persistence;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.persistence.TypedQuery;
import javax.persistence.Version;

@MappedSuperclass
public abstract class BasicEntity {
//	public static final String FIND_ALL = "findAllPomodoros";

	@Transient 
	EntityManager em = null;
	@Transient 
	EntityManagerFactory emf = null;
	@Transient 
	EntityTransaction tx = null;
	@Transient 
	public String sqlitedbid = "";
	@Transient 
	public void begin(){
		String test = getSqlitedbid();
		emf = Persistence.createEntityManagerFactory(test);
		em = emf.createEntityManager();
		tx = em.getTransaction();
		tx.begin();

	}
	@Transient 
	public void close(){
		em.close();
		emf.close();	
	}
	@Transient 
	public void save(){
		begin();
		em.persist(this);;
		tx.commit();
		close();

	}
	@Transient 
	public Object find(Long id){
		begin();
		Object found = em.find(getClazz(), id.longValue());
		System.out.println("PERSISTED OBJECT: " + found);
		close();
		return found;
	}

	@Transient 
	public List list(){
		begin();
		TypedQuery query = em.createQuery("From " + getClassName() ,getClazz());
		close();
		return query.getResultList();
	}
	

	@Transient 
	public abstract String getClassName();
	@Transient 
	public abstract Class getClazz();
	@Transient 
	public abstract String getSqlitedbid();
	
	@Id
	@GeneratedValue
	private Long id;
	@Version
	private Long version;
	private Date createdAt;
	private Date updatedAt;

	public Long getId() {
		return id;
	}
	
	public Long getVersion() {
		return version;
	}
	
	public void setVersion(Long version) {
		this.version = version;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	@PrePersist
	public void onCreate() {
		createdAt = new Date();
		onUpdate();
	}
	
	@PreUpdate
	public void onUpdate() {
		updatedAt = new Date();
	}
}