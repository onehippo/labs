<#include "../include/imports.ftl">
<div class="body-wrapper">
  <div class="container">
    <div class="row">
      <div class="col-md-9 col-sm-9">
        <#if pageable??>
          <#if pageable.total == 0>
            <h4>No results for '${query?html}'</h4>
          <#else>
            <h4>Displaying  ${pageable.startOffset +1 } to ${pageable.endOffset} of ${pageable.total} results for '${query?html}'</h4>
            <div id="search-results">
            <#list pageable.items as item>
              <@hst.link var="link" hippobean=item />
                <div class="blog-post">
                  <div class="blog-post-type">
                    <i class="icon-shop"> </i>
                  </div>
                  <div class="blog-span">
                    <h2>
                      <a href="${link}">${item.title?html}</a>
                    </h2>
                    <div class="blog-post-body">
                      <p>${item.introduction?html}</p>
                    </div>
                    <div class="blog-post-details">
                      <div class="blog-post-details-item blog-post-details-item-right">
                        <a href="${link}"> Read more<i
                          class="fa fa-chevron-right"></i>
                        </a>
                      </div>
                    </div>
                  </div>
                </div>
            </#list>
          </div>
        <#if cparam.showPagination>
          <#include "../include/pagination.ftl">
        </#if>
      </#if>
    <#else>
      <h3>Please fill in a search term.</h3>
    </#if>
      <div class="col-md-3 col-sm-3">
      </div>
    </div>
  </div>
</div>