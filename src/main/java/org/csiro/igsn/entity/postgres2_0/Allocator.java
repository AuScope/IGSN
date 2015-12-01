package org.csiro.igsn.entity.postgres2_0;

// Generated 27/10/2015 10:58:13 AM by Hibernate Tools 4.3.1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Allocator generated by hbm2java
 */
@Entity
@Table(name = "allocator")
@NamedQueries({
	@NamedQuery(
			name="Allocator.search",
		    query="SELECT a FROM Allocator a where a.username = :username"
	)
})	
public class Allocator implements java.io.Serializable {

	private int allocatorid;
	private String comments;
	private String contactemail;
	private String contactname;
	private Date created;
	private String username;
	private String password;
	private Boolean isactive;
	private Set<Prefix> prefixes = new HashSet<Prefix>(0);
	private Set<Registrant> registrants = new HashSet<Registrant>(0);

	public Allocator() {
	}

	public Allocator(int allocatorid, String contactemail, String contactname,
			String username) {
		this.allocatorid = allocatorid;
		this.contactemail = contactemail;
		this.contactname = contactname;
		this.username = username;
	}

	public Allocator(int allocatorid, String comments, String contactemail,
			String contactname, Date created, String username, String password,
			Boolean isactive, Set<Prefix> prefixes, Set<Registrant> registrants) {
		this.allocatorid = allocatorid;
		this.comments = comments;
		this.contactemail = contactemail;
		this.contactname = contactname;
		this.created = created;
		this.username = username;
		this.password = password;
		this.isactive = isactive;
		this.prefixes = prefixes;
		this.registrants = registrants;
	}

	@Id
	@Column(name = "allocatorid", unique = true, nullable = false)
	public int getAllocatorid() {
		return this.allocatorid;
	}

	public void setAllocatorid(int allocatorid) {
		this.allocatorid = allocatorid;
	}

	@Column(name = "comments")
	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@Column(name = "contactemail", nullable = false)
	public String getContactemail() {
		return this.contactemail;
	}

	public void setContactemail(String contactemail) {
		this.contactemail = contactemail;
	}

	@Column(name = "contactname", nullable = false, length = 80)
	public String getContactname() {
		return this.contactname;
	}

	public void setContactname(String contactname) {
		this.contactname = contactname;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created", length = 29)
	public Date getCreated() {
		return this.created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Column(name = "username", nullable = false, length = 50)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "password", length = 50)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "isactive")
	public Boolean getIsactive() {
		return this.isactive;
	}

	public void setIsactive(Boolean isactive) {
		this.isactive = isactive;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "allocator_prefixes", joinColumns = { @JoinColumn(name = "allocator", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "prefixes", nullable = false, updatable = false) })
	public Set<Prefix> getPrefixes() {
		return this.prefixes;
	}

	public void setPrefixes(Set<Prefix> prefixes) {
		this.prefixes = prefixes;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "allocator")
	public Set<Registrant> getRegistrants() {
		return this.registrants;
	}

	public void setRegistrants(Set<Registrant> registrants) {
		this.registrants = registrants;
	}

}
