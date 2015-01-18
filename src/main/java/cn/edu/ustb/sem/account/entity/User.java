package cn.edu.ustb.sem.account.entity;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import cn.edu.ustb.sem.core.auth.bo.Visitor;
import cn.edu.ustb.sem.schedule.entity.Worker;
@Entity
@Table(name = "sys_user")
public class User implements Serializable {
	private static final long serialVersionUID = 9025165184197170266L;
	public static final Byte DELETED = 1;
	public static final Byte NOT_DELETED = 0;
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GenericGenerator(name = "generator", strategy = "native")
	@GeneratedValue(generator = "generator")
	private Integer id;
	@Column(name = "username")
	private String userName;
	@Column(name = "password")
	private String password;
	@ManyToOne
	@JoinColumn(name="role_id")
	@NotFound(action=NotFoundAction.IGNORE)
	@LazyToOne(LazyToOneOption.PROXY)
	private Role role;
	@Column(name = "udate", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar udate;
	@ManyToOne
	@JoinColumn(name="uuid")
	@NotFound(action=NotFoundAction.IGNORE)
	@LazyToOne(LazyToOneOption.PROXY)
	private User updater;
	
	@Column(name = "nick_name")
	private String nickName;
	
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	@OneToOne(mappedBy="user",fetch=FetchType.EAGER) 
	private Worker worker;
	
	public Worker getWorker() {
		return worker;
	}
	public void setWorker(Worker worker) {
		this.worker = worker;
	}
	@Column(name = "is_banded")
	private Byte isBanded;
	
	@Column(name = "is_deleted")
	private Byte isDeleted = NOT_DELETED;
	
	public Byte getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Byte isDeleted) {
		this.isDeleted = isDeleted;
	}
	public User() {};
	public User(Integer uid) {
		this.id = uid;
	}
	public User(Visitor v) {
		this.id = v.getUid();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return userName + ":" + password;
	}
	public Calendar getUdate() {
		return udate;
	}
	public void setUdate(Calendar udate) {
		this.udate = udate;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public User getUpdater() {
		return updater;
	}
	public void setUpdater(User updater) {
		this.updater = updater;
	}
	public Byte getIsBanded() {
		return isBanded;
	}
	public void setIsBanded(Byte isBanded) {
		this.isBanded = isBanded;
	}
}
