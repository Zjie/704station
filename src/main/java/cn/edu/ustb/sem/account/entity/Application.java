package cn.edu.ustb.sem.account.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
@Entity
@Table(name = "sys_application")
public class Application implements Serializable {

	private static final long serialVersionUID = 8815934959095733585L;
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GenericGenerator(name = "generator", strategy = "native")
	@GeneratedValue(generator = "generator")
	private Integer id;
	@Column(name = "name")
	private String name;
	@Column(name = "is_menu")
	private Byte isMenu;
	
	@ManyToOne
	@JoinColumn(name="pid")
	@LazyToOne(LazyToOneOption.FALSE)
	@NotFound(action=NotFoundAction.IGNORE)
	private Application parent;
	
	@ManyToMany  
    @JoinTable(name = "sys_app_app", joinColumns = @JoinColumn(name = "app_parent_id"), inverseJoinColumns = @JoinColumn(name = "app_child_id"))
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<Application> childs;
	@Column(name = "level")
	private Integer level;
	@Column(name = "remark")
	private String remark;
	@Column(name = "menu_name")
	private String menuName;
	@Column(name = "menu_url")
	private String menuUrl;
	
	@ManyToMany  
    @JoinTable(name = "sys_role_app", joinColumns = @JoinColumn(name = "app_id"), inverseJoinColumns = @JoinColumn(name = "role_id")) 
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<Role> roles;
	
	@OneToMany(mappedBy = "app", fetch = FetchType.LAZY, targetEntity=Url.class)
	private Set<Url> urls;
	@Column(name = "udate", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar udate;
	@ManyToOne
	@JoinColumn(name="uuid")
	@NotFound(action=NotFoundAction.IGNORE)
	@LazyToOne(LazyToOneOption.PROXY)
	private User updater;
	
	public User getUpdater() {
		return updater;
	}
	public void setUpdater(User updater) {
		this.updater = updater;
	}

	public Calendar getUdate() {
		return udate;
	}
	public void setUdate(Calendar udate) {
		this.udate = udate;
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Application getParent() {
		return parent;
	}

	public void setParent(Application parent) {
		this.parent = parent;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Set<Application> getChilds() {
		return childs;
	}

	public void setChilds(Set<Application> childs) {
		this.childs = childs;
	}

	@Override
	public String toString() {
		return name;
	}

	public Set<Url> getUrls() {
		return urls;
	}

	public void setUrls(Set<Url> urls) {
		this.urls = urls;
	}

	public Byte getIsMenu() {
		return isMenu;
	}

	public void setIsMenu(Byte isMenu) {
		this.isMenu = isMenu;
	}
	
}
