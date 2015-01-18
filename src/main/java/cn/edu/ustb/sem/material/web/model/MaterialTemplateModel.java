package cn.edu.ustb.sem.material.web.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.edu.ustb.sem.account.entity.User;
import cn.edu.ustb.sem.core.util.DateUtil;
import cn.edu.ustb.sem.material.entity.Material;
import cn.edu.ustb.sem.material.entity.MaterialTemplate;
import cn.edu.ustb.sem.material.entity.Mtm;

public class MaterialTemplateModel {
	private int id;
	private String name;
	private String groupName;
	private String productCode;
	private String udate;
	private String updater;
	private List<MaterialModel> materials;
	public MaterialTemplateModel(){}
	public MaterialTemplateModel(MaterialTemplate bo) {
		this.id = bo.getId();
		this.name = bo.getName();
		this.groupName = bo.getGroupName();
		this.productCode = bo.getProductCodeString();
		this.udate = DateUtil.getDateTime(bo.getUdate());
		User updater = bo.getUpdater();
		if (updater != null) {
			this.updater = bo.getUpdater().getUserName();
		}
		List<MaterialModel> mms = new ArrayList<MaterialModel>();
		Set<Mtm> mtms = bo.getMtms();
		if (mtms != null) {
			for (Mtm mtm : mtms) {
				Material m = mtm.getMaterial();
				MaterialModel mm = new MaterialModel(m);
				mms.add(mm);
			}
		}
		this.materials = mms;
	}
	public List<MaterialModel> getMaterials() {
		return materials;
	}
	public void setMaterials(List<MaterialModel> materials) {
		this.materials = materials;
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
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getUdate() {
		return udate;
	}
	public void setUdate(String udate) {
		this.udate = udate;
	}
	public String getUpdater() {
		return updater;
	}
	public void setUpdater(String updater) {
		this.updater = updater;
	}
	
}
