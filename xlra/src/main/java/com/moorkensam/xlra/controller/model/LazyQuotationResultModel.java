package com.moorkensam.xlra.controller.model;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.moorkensam.xlra.model.rate.QuotationResult;
import com.moorkensam.xlra.service.QuotationService;

public class LazyQuotationResultModel extends LazyDataModel<QuotationResult> {

	private static final long serialVersionUID = 8411957900735285265L;

	private final QuotationService quotationService;

	private List<QuotationResult> dataSource;

	public LazyQuotationResultModel(QuotationService service) {
		this.quotationService = service;
	}

	@Override
	public QuotationResult getRowData(String rowKey) {
		for (QuotationResult result : dataSource) {
			if (result.getId() == Long.parseLong(rowKey)) {
				return result;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(QuotationResult object) {
		return object.getId();
	}

	@Override
	public List<QuotationResult> load(int first, int pageSize,
			String sortField, SortOrder sortOrder, Map<String, String> filters) {
		return quotationService.getQuotationQueries(first, pageSize, sortField,
				sortOrder, filters);
	}

	public List<QuotationResult> getDataSource() {
		return dataSource;
	}

	public void setDataSource(List<QuotationResult> dataSource) {
		this.dataSource = dataSource;
	}
}
