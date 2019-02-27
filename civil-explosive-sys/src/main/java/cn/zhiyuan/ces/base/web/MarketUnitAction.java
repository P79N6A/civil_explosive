package cn.zhiyuan.ces.base.web;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.zhiyuan.ces.base.biz.IMarketUnitBiz;
import cn.zhiyuan.ces.base.biz.IUploadResourceBiz;
import cn.zhiyuan.ces.base.entity.MarketUnit;
import cn.zhiyuan.frame.BaseAction;
import cn.zhiyuan.frame.CommonUtils;
import cn.zhiyuan.frame.MsgBean;
import cn.zhiyuan.frame.PageBean;
import cn.zhiyuan.frame.orm.builder.IWhereSql;

/**
 * 
 * 爆炸物品销售单位
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/base")
public final class MarketUnitAction extends BaseAction{

	@Autowired
	private IMarketUnitBiz iMarketUnitBiz;
	
	@Autowired
	private IUploadResourceBiz iUploadResourceBiz;
	
	@RequestMapping(value="/marketunit/jsons") 
	public ModelAndView json(PageBean<MarketUnit> pb,MarketUnit marketUnit,final String q) {
		iMarketUnitBiz.getPageList(pb,marketUnit,null,new IWhereSql() {
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

	@RequestMapping("/marketunit/boxJson") 
	public ModelAndView boxJson(MsgBean mbean) {
		mbean.setObj(iMarketUnitBiz.getList(null,null));
		return json(mbean);
	}
	
	@RequestMapping("/marketunit/save") 
	public ModelAndView save(MsgBean mbean,MarketUnit marketUnit,String uploadIds) { 
		mbean.setInfo("保存成功");
		if(marketUnit.getId() == null) {
			iMarketUnitBiz.addAndId(marketUnit);
			iUploadResourceBiz.updateHostId(marketUnit.getId(),
						CommonUtils.str2IntList(uploadIds));
		}else iMarketUnitBiz.update(marketUnit);
		return json(mbean);
	}

	@RequestMapping("/marketunit/delete") 
	public ModelAndView delete(MsgBean mbean,MarketUnit marketUnit) {
		mbean.setInfo("删除成功");
		iMarketUnitBiz.delete(marketUnit);
		return json(mbean);
	}

}
