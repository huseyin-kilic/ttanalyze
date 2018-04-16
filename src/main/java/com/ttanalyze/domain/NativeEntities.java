/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ttanalyze.domain;

import org.springframework.social.twitter.api.HashTagEntity;
import org.springframework.social.twitter.api.MentionEntity;
import org.springframework.social.twitter.api.TickerSymbolEntity;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>A json representation of entities found within twitter status objects.<p>
 * @author bowen
 */
public class NativeEntities implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List<UrlEntity> urls = new LinkedList<>();
	
	private List<HashTagEntity> hashtags = new LinkedList<>();
	
	private List<MentionEntity> userMentions = new LinkedList<>();
	
	private List<TickerSymbolEntity> symbols = new LinkedList<>();

	public NativeEntities(List<UrlEntity> urls, List<HashTagEntity> tags, List<MentionEntity> mentions, List<TickerSymbolEntity> symbols) {
		this.urls = urls;
		this.hashtags = tags;
		this.userMentions = mentions;
		this.symbols = symbols;
	}

	public List<UrlEntity> getUrls() {
		if (this.urls == null) {
			return Collections.emptyList();
		}
		return this.urls;
	}
	
	
	public List<HashTagEntity> getHashTags() {
		if (this.hashtags == null) {
			return Collections.emptyList();
		}
		return this.hashtags;
	}
	
	
	public List<MentionEntity> getUserMentions() {
		if (this.userMentions == null) {
			return Collections.emptyList();
		}
		return this.userMentions;
	}
	

	public List<TickerSymbolEntity> getSymbols() {
		if (this.symbols == null) {
			return Collections.emptyList();
		}
		return this.symbols;
	}
	
	public boolean hasUrls() {
		return this.urls != null && !this.urls.isEmpty();
	}
	
	
	public boolean hasTags() {
		return this.hashtags != null && !this.hashtags.isEmpty();
	}
	
	
	public boolean hasMentions() {
		return this.userMentions != null && !this.userMentions.isEmpty();
	}


	public boolean hasTickerSymbols() {
		return this.symbols != null && !this.symbols.isEmpty();
	}

}
