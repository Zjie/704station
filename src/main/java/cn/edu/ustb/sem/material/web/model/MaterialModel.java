package cn.edu.ustb.sem.material.web.model;

import cn.edu.ustb.sem.material.entity.Material;
import cn.edu.ustb.sem.material.entity.MaterialTemplate;

public class MaterialModel {
	private int id;
	private String name;
	private String type;
	private String specification;
	private String level;
	private String uom;
	private double singleNum;
	private double bkNum;
	private int mtId;
	private int no;
	
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public MaterialModel(){}
	public MaterialModel(Material m) {
		if (m == null) {
			return;
		}
		this.id = m.getId();
		this.name = m.getName();
		this.type = m.getType();
		this.specification = m.getSpecification();
		if (m.getLevel() != null)
			this.level = m.getLevel();
		this.uom = m.getUom();
		this.singleNum = m.getSingleNum();
		if (m.getBackupNum() != null)
			this.bkNum = m.getBackupNum();
		MaterialTemplate mt = m.getMtm().getMt();
		if (mt != null) {
			this.mtId = mt.getId();
		}
	}
	public int getMtId() {
		return mtId;
	}
	public void setMtId(int mtId) {
		this.mtId = mtId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSpecification() {
		return specification;
	}
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getUom() {
		return uom;
	}
	public void setUom(String uom) {
		this.uom = uom;
	}
	public double getSingleNum() {
		return singleNum;
	}
	public void setSingleNum(int singleNum) {
		this.singleNum = singleNum;
	}
	public double getBkNum() {
		return bkNum;
	}
	public void setBkNum(int bkNum) {
		this.bkNum = bkNum;
	}
	
}
