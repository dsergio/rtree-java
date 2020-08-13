package com.dsergio.rtreeapiboot.controllers;

import co.dsergio.rtree.business.services.generic.RTreeServiceBaseGeneric;
import rtree.item.generic.IRType;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
public abstract class RTreeControllerBaseGeneric<T extends IRType<T>> {
	
	protected RTreeServiceBaseGeneric<T> rtreeService;
	
	public RTreeControllerBaseGeneric(RTreeServiceBaseGeneric<T> rtreeService) {
		this.rtreeService = rtreeService;
	}
	
}
