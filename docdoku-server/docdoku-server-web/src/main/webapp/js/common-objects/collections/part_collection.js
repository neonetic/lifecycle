define([
    "common-objects/models/part"
], function (
    Part
    ) {
    var PartList = Backbone.Collection.extend({

        model: Part,

        className:"PartList",

        initialize:function(start){

            this.currentPage= 0;
            this.pageCount = 0;
            this.resultsPerPage = 20;

            this.urlBase = "/api/workspaces/" + APP_CONFIG.workspaceId + "/parts?start="

            if(start){
                this.currentPage=start;
            }

        },

        fetchPageCount:function(){
            var self = this ;
            $.ajax({
                url:"/api/workspaces/" + APP_CONFIG.workspaceId + "/parts/count",
                success:function(data){
                    self.pageCount = Math.ceil(data/self.resultsPerPage);
                    self.trigger("page-count:fetch");
                },
                error:function(){
                    self.trigger("page-count:fetch");
                }
            });
        },

        hasSeveralPages:function(){
            return this.pageCount > 1 ;
        },

        setCurrentPage:function(page){
            this.currentPage=page;
            return this;
        },

        getPageCount:function(){
            return this.pageCount;
        },

        getCurrentPage:function(){
            return this.currentPage + 1 ;
        },

        isLastPage:function(){
            return this.currentPage >= this.pageCount - 1 ;
        },

        isFirstPage:function(){
            return this.currentPage <= 0 ;
        },

        setFirstPage:function(){
            if(!this.isFirstPage()){
                this.currentPage = 0;
            }
            return this;
        },

        setLastPage:function(){
            if(!this.isLastPage()){
                this.currentPage = this.pageCount -1 ;
            }
            return this;
        },

        setNextPage:function(){
            if(!this.isLastPage()){
                this.currentPage++;
            }
            return this;
        },

        setPreviousPage:function(){
            if(!this.isFirstPage()){
                this.currentPage--;
            }
            return this;
        },

        url:function(){
            return this.urlBase + this.currentPage * this.resultsPerPage;
        }

    });

    return PartList;
});
