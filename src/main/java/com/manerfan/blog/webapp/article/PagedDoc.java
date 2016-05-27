/*
 * ManerFan(http://www.manerfan.com). All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.manerfan.blog.webapp.article;

import org.apache.lucene.search.ScoreDoc;

/**
 * <pre>
 * 解决ScoreDoc没有默认构造函数的问题
 * </pre>
 *
 * @author ManerFan 2016年5月24日
 */
public class PagedDoc extends ScoreDoc {

    public PagedDoc() {
        super(-1, -1f);
    }

    public PagedDoc(int doc, float score) {
        super(doc, score);
    }

    public PagedDoc(int doc, float score, int shardIndex) {
        super(doc, score, shardIndex);
    }

    public void setScore(float score) {
        this.score = score;
    }

    public void setDoc(int doc) {
        this.doc = doc;
    }

    public void setShardIndex(int shardIndex) {
        this.shardIndex = shardIndex;
    }

}
