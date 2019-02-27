package cn.zhiyuan.ces.base.web;

import cn.zhiyuan.ces.base.biz.IDelegationUnitBiz;
import cn.zhiyuan.ces.base.biz.IUploadResourceBiz;
import cn.zhiyuan.ces.base.entity.DelegationUnit;
import org.springframework.stereotype.Controller;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import cn.zhiyuan.frame.*;
import cn.zhiyuan.frame.orm.builder.IWhereSql;


@Controller
@RequestMapping("/base")
public final class DelegationUnitAction extends BaseAction{

	@Autowired
	private IDelegationUnitBiz iDelegationUnitBiz;
	
	@Autowired
	private IUploadResourceBiz iUploadResourceBiz;
	
	@RequestMapping("/delegationunit/jsons") 
	public ModelAndView json(PageBean<DelegationUnit> pb,DelegationUnit delegationUnit,final String q) {
		iDelegationUnitBiz.getPageList(pb,delegationUnit,null,new IWhereSql() {
			@Override
			public void where(StringBuilder sb, Map<String, Object> params) {
				if(StringUtils.isNotEmpty(q)) {
					sb.append(" and t.name like :q ");
					params.put("q", "%" + q + "%");
				}
			}
		});
		return json(pb);
	}

	@RequestMapping("/delegationunit/save") 
	public ModelAndView save(MsgBean mbean,DelegationUnit delegationUnit,String uploadIds) {
		mbean.setInfo("保存成功");
		if(null == delegationUnit.getId()) {
			iDelegationUnitBiz.addAndId(delegationUnit);
			iUploadResourceBiz.updateHostId(delegationUnit.getId(),
					CommonUtils.str2IntList(uploadIds));
		}else iDelegationUnitBiz.update(delegationUnit);
		return json(mbean);
	}

	@RequestMapping("/delegationunit/delete") 
	public ModelAndView delete(MsgBean mbean,DelegationUnit delegationUnit) {
		mbean.setInfo("删除成功");
		iDelegationUnitBiz.delete(delegationUnit);
		return json(mbean);
	}

}
