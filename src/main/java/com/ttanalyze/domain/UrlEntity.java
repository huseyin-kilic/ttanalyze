/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ttanalyze.domain;

import java.io.Serializable;
import java.util.Arrays;

/**
 * <p>A representation of a URL found within a tweet entity.</p>
 * @author bowen
 */
public class UrlEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String displayUrl;

	private String expandedUrl;

	private String url;

	private int[] indices;

	public UrlEntity(String displayUrl, String expandedUrl, String url, int[] indices) {
		this.displayUrl = displayUrl;
		this.expandedUrl = expandedUrl;
		this.url = url;
		this.indices = indices;
	}


	public String getDisplayUrl() {
		return this.displayUrl;
	}


	public String getExpandedUrl() {
		return this.expandedUrl;
	}


	public String getUrl() {
		return this.url;
	}


	public int[] getIndices() {
		if (this.indices == null || this.indices.length <= 0) {
			return new int[0];
		}
		return this.indices;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		UrlEntity urlEntity = (UrlEntity) o;
		if (displayUrl != null ? !displayUrl.equals(urlEntity.displayUrl) : urlEntity.displayUrl != null) {
			return false;
		}
		if (expandedUrl != null ? !expandedUrl.equals(urlEntity.expandedUrl) : urlEntity.expandedUrl != null) {
			return false;
		}
		if (!Arrays.equals(indices, urlEntity.indices)) {
			return false;
		}
		if (url != null ? !url.equals(urlEntity.url) : urlEntity.url != null) {
			return false;
		}
		return true;
	}


	@Override
	public int hashCode() {
		int result = displayUrl != null ? displayUrl.hashCode() : 0;
		result = 31 * result + (expandedUrl != null ? expandedUrl.hashCode() : 0);
		result = 31 * result + (url != null ? url.hashCode() : 0);
		result = 31 * result + (indices != null ? Arrays.hashCode(indices) : 0);
		return result;
	}
}
