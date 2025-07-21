package com.dsergio.rtreeapiboot.controllers;

import co.dsergio.rtree.business.services.RTreeServiceBase;
import rtree.item.IRType;

public abstract class RTreeControllerBaseGeneric<T extends IRType<T>> {
	
	protected RTreeServiceBase<T> rtreeService;
	
	public RTreeControllerBaseGeneric(RTreeServiceBase<T> rtreeService) {
		this.rtreeService = rtreeService;
	}
	
}
