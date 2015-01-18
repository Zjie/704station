package cn.edu.ustb.sem.process.dao.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Repository;

import cn.edu.ustb.sem.core.dao.impl.BaseDaoImpl;
import cn.edu.ustb.sem.process.dao.PtProductCodeDao;
import cn.edu.ustb.sem.process.entity.ProcessTemplate;
import cn.edu.ustb.sem.process.entity.PtProductCode;
@Repository("ptProductCodeDao")
public class PtProductCodeDaoImpl extends BaseDaoImpl<PtProductCode, Integer> implements PtProductCodeDao {

	@Override
	public void updateProductCodeForPt(ProcessTemplate pt) {
		//先删除已有的产品代号
		PtProductCode model = new PtProductCode();
		model.setPt(new ProcessTemplate(pt.getId()));
		this.deleteObject(model);
		//再增加新的productCode
		Set<PtProductCode> result = new HashSet<PtProductCode>();
		String[] procods = pt.getProductCodeString().split(",");
		for (String pc : procods) {
			if (pc.trim().equals("")) {
				continue;
			}
			Set<PtProductCode> pcs = parseProductCode(pc, pt);
			result.addAll(pcs);
		}
		for (PtProductCode ppc : result) {
			this.save(ppc);
		}
	}
	/**
	 * 解析产品代号系列得到所有产品代号
	 * 例如：cx-1-2/5={cx-1-2, cx-1-3, cx-1-4, cx-1-5}
	 * @param productCode
	 * @return
	 */
	private Set<PtProductCode> parseProductCode(String productCode, ProcessTemplate pt) {
		Set<PtProductCode> pcs = new HashSet<PtProductCode>();
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
					PtProductCode pc = new PtProductCode();
					pc.setProductCode(newProductCode);
					pc.setPt(pt);
					pcs.add(pc);
					begin++;
				}
			}
		} else {
			PtProductCode pc = new PtProductCode();
			pc.setProductCode(productCode);
			pc.setPt(pt);
			pcs.add(pc);
		}
		return pcs;
	}
}
