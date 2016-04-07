define([
    "underscore",
    "js/editor/utils",
    "js/editor/storage",
], function (_, utils, storage) {

    function FileDescriptor(fileIndex, title, publishLocations) {
        this._fileIndex = fileIndex;
        this._title = title || storage[fileIndex + ".title"]; // 标题
        this._editorScrollTop = parseInt(storage[fileIndex + ".editorScrollTop"]) || 0;
        this._editorStart = parseInt(storage[fileIndex + ".editorEnd"]) || 0;
        this._editorEnd = parseInt(storage[fileIndex + ".editorEnd"]) || 0;
        this._previewScrollTop = parseInt(storage[fileIndex + ".previewScrollTop"]) || 0;
        this._selectTime = parseInt(storage[fileIndex + ".selectTime"]) || 0;
        this.publishLocations = publishLocations || {};

        this._uid = parseInt(storage[fileIndex + ".uid"]) || null; // 文章id
        this._summary = storage[fileIndex + ".summary"] || ""; // 摘要
        this._categories = utils.retrieveIndexArray(fileIndex + ".categories"); // 分类

        Object.defineProperty(this, 'fileIndex', {
            get: function () {
                return this._fileIndex;
            },
            set: function (fileIndex) {
                var indexOld = this._fileIndex;
                var publishKey = indexOld + ".publish";
                _.each(_.keys(localStorage), function (key) {
                    if (utils.startWith(key, indexOld)) {
                        if (publishKey == key) {
                            _.each(utils.retrieveIndexArray(key), function (_key) {
                                /* 移除 publish.* */
                                localStorage.removeItem(_key);
                            });
                        }
                        /* 替换key */
                        localStorage.setItem(key.replace(indexOld, fileIndex), localStorage[key]);
                        localStorage.removeItem(key);
                    }
                });

                this._fileIndex = fileIndex;
            }
        });

        Object.defineProperty(this, 'uid', {
            get: function () {
                return this._uid;
            },
            set: function (uid) {
                this._uid = uid;
                if (!!this._fileIndex) {
                    storage[this._fileIndex + ".uid"] = uid;
                }
            }
        });

        Object.defineProperty(this, 'summary', {
            get: function () {
                return this._summary;
            },
            set: function (summary) {
                this._summary = summary;
                if (!!this._fileIndex) {
                    storage[this._fileIndex + ".summary"] = summary;
                }
            }
        });

        Object.defineProperty(this, 'categories', {
            get: function () {
                return this._categories;
            },
            set: function (categories) {
                var _categories = this._categories = [];
                _.each(categories, function (category) {
                    _categories.push(category.toLowerCase());
                });
                if (!!this._fileIndex) {
                    utils.resetIndexToArray(this._fileIndex + ".categories", this._categories);
                }
            }
        });

        Object.defineProperty(this, 'title', {
            get: function () {
                return this._title;
            },
            set: function (title) {
                this._title = title;
                if (!!this._fileIndex) {
                    storage[this._fileIndex + ".title"] = title;
                }
            }
        });
        Object.defineProperty(this, 'content', {
            get: function () {
                return storage[this._fileIndex + ".content"];
            },
            set: function (content) {
                if (!!this._fileIndex) {
                    storage[this._fileIndex + ".content"] = content;
                }
            }
        });
        Object.defineProperty(this, 'editorScrollTop', {
            get: function () {
                return this._editorScrollTop;
            },
            set: function (editorScrollTop) {
                this._editorScrollTop = editorScrollTop;
                if (!!this._fileIndex) {
                    storage[this._fileIndex + ".editorScrollTop"] = editorScrollTop;
                }
            }
        });
        Object.defineProperty(this, 'editorStart', {
            get: function () {
                return this._editorStart;
            },
            set: function (editorStart) {
                this._editorStart = editorStart;
                if (!!this._fileIndex) {
                    storage[this._fileIndex + ".editorStart"] = editorStart;
                }
            }
        });
        Object.defineProperty(this, 'editorEnd', {
            get: function () {
                return this._editorEnd;
            },
            set: function (editorEnd) {
                this._editorEnd = editorEnd;
                if (!!this._fileIndex) {
                    storage[this._fileIndex + ".editorEnd"] = editorEnd;
                }
            }
        });
        Object.defineProperty(this, 'previewScrollTop', {
            get: function () {
                return this._previewScrollTop;
            },
            set: function (previewScrollTop) {
                this._previewScrollTop = previewScrollTop;
                if (!!this._fileIndex) {
                    storage[this._fileIndex + ".previewScrollTop"] = previewScrollTop;
                }
            }
        });
        Object.defineProperty(this, 'selectTime', {
            get: function () {
                return this._selectTime;
            },
            set: function (selectTime) {
                this._selectTime = selectTime;
                if (!!this._fileIndex) {
                    storage[this._fileIndex + ".selectTime"] = selectTime;
                }
            }
        });
    }

    FileDescriptor.prototype.addPublishLocation = function (publishAttributes) {
        utils.storeAttributes(publishAttributes);
        utils.appendIndexToArray(this._fileIndex + ".publish", publishAttributes.publishIndex);
        this.publishLocations[publishAttributes.publishIndex] = publishAttributes;
    };

    FileDescriptor.prototype.removePublishLocation = function (publishAttributes) {
        utils.removeIndexFromArray(this._fileIndex + ".publish", publishAttributes.publishIndex);
        delete this.publishLocations[publishAttributes.publishIndex];
    };

    function addIcon(result, attributes) {
        result.push('<i class="icon-provider-' + attributes.provider.providerId + '"></i>');
    }

    function addSyncIconWithLink(result, attributes) {
        if (attributes.provider.getSyncLocationLink) {
            var syncLocationLink = attributes.provider.getSyncLocationLink(attributes);
            result.push([
                '<a href="',
                syncLocationLink,
                '" target="_blank" title="Open in ',
                attributes.provider.providerName,
                '"><i class="icon-provider-',
                attributes.provider.providerId,
                '"></i><i class="icon-link-ext-alt"></i></a>'
            ].join(''));
        }
        else {
            addIcon(result, attributes);
        }
    }

    function addPublishIconWithLink(result, attributes) {
        if (attributes.provider.getPublishLocationLink) {
            var publishLocationLink = attributes.provider.getPublishLocationLink(attributes);
            result.push([
                '<a href="',
                publishLocationLink,
                '" target="_blank" title="Open in ',
                attributes.provider.providerName,
                '"><i class="icon-provider-',
                attributes.provider.providerId,
                '"></i><i class="icon-link-ext-alt"></i></a>'
            ].join(''));
        }
        else {
            addIcon(result, attributes);
        }
    }

    FileDescriptor.prototype.composeTitle = function (createLinks) {
        var result = [];
        var addSyncIcon = createLinks ? addSyncIconWithLink : addIcon;
        var addPublishIcon = createLinks ? addPublishIconWithLink : addIcon;
        _.chain(this.syncLocations).sortBy(function (attributes) {
            return attributes.provider.providerId;
        }).each(function (attributes) {
            addSyncIcon(result, attributes);
        });
        if (_.size(this.syncLocations) !== 0) {
            result.push('<i class="icon-refresh title-icon-category"></i>');
        }
        _.chain(this.publishLocations).sortBy(function (attributes) {
            return attributes.provider.providerId;
        }).each(function (attributes) {
            addPublishIcon(result, attributes);
        });
        if (_.size(this.publishLocations) !== 0) {
            result.push('<i class="icon-upload title-icon-category"></i>');
        }
        result.push(_.escape(this.title));
        return result.join('');
    };

    return FileDescriptor;
});
