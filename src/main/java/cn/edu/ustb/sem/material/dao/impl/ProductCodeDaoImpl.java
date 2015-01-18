package cn.edu.ustb.sem.material.dao.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.edu.ustb.sem.core.dao.impl.BaseDaoImpl;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.material.dao.ProductCodeDao;
import cn.edu.ustb.sem.material.entity.MaterialTemplate;
import cn.edu.ustb.sem.material.entity.MtProductCode;

@Repository("productCodeDao")
public class ProductCodeDaoImpl extends BaseDaoImpl<MtProductCode, Integer> implements ProductCodeDao {

	@Override
	public void updateProductCode4Mt(MaterialTemplate mt)
			throws ServiceException {
		//先删除已有的产品代号
		Query query = this.getSession().createQuery("delete from " + MtProductCode.class.getSimpleName() + " p where p.mt.id = ?");
		query.setParameter(0, mt.getId());
		query.executeUpdate();
		Set<MtProductCode> result = new HashSet<MtProductCode>();
		//再增加新的productCode
		String[] procods = mt.getProductCodeString().split(",");
		for (String pc : procods) {
			pc = pc.toLowerCase().trim();
			if (pc.equals("")) {
				continue;
			}
			Set<MtProductCode> pcs = parseProductCode(pc, mt);
			result.addAll(pcs);
		}
		for (MtProductCode pc : result) {
			this.save(pc);
		}
	}
	/**
	 * 解析产品代号系列得到所有产品代号
	 * 例如：cx-1-2/5={cx-1-2, cx-1-3, cx-1-4, cx-1-5}
	 * @param productCode
	 * @return
	 */
	private Set<MtProductCode> parseProductCode(String productCode, MaterialTemplate mt) {
		Set<MtProductCode> pcs = new HashSet<MtProductCode>();
		Pattern pattern = Pattern.compile("\\d+/\\d+");
		Matcher matcher = pattern.matcher(productCode);
		if (matcher.find()) {
			String rex = matcher.group();
			String prefix = productCode.substring(0, productCode.length() - rex.length());
			String[] be = rex.split("/");
			int begin = Integer.parseInt(be[0]);
			int end = Integer.parseInt(be[1]);
			if (begin > end) {
				//do some thing
			} else {
				while (begin <= end) {
					String newProductCode = prefix + begin;
					MtProductCode pc = new MtProductCode();
					pc.setProductCode(newProductCode.toLowerCase().trim());
					pc.setMt(mt);
					pcs.add(pc);
					begin++;
				}
			}
		} else {
			MtProductCode pc = new MtProductCode();
			pc.setProductCode(productCode);
			pc.setMt(mt);
			pcs.add(pc);
		}
		return pcs;
	}
	@Override
	public boolean exists(String pc) throws ServiceException {
		MtProductCode model = new MtProductCode();
		model.setProductCode(pc);
		return this.find(model) != null;
	}
}
