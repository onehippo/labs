<#include "../include/imports.ftl">
<div class="col-md-9 col-sm-9">
  <div class="news-overview">
    <#if pageable?? && pageable.items?has_content>
      <#list pageable.items as item>
        <@hst.link var="link" hippobean=item />
          <div class="blog-post">
            <@hst.cmseditlink hippobean=item/>
            <div class="blog-post-type">
              <i class="icon-news"> </i>
            </div>
            <div class="blog-span">
            <#if item.image?? && item.image.large??>              
              <@hst.link var="img" hippobean=item.image.large />
              <div class="blog-post-featured-img">
                <a href="${link}"><img
                  src="${img}"
                  alt="${item.title}" /></a>
              </div>
            </#if>                  
            <h2>
              <a href="${link}">${item.title}</a>
            </h2>
            <div class="blog-post-body">
              <p>${item.introduction}</p>
            </div>
            <div class="blog-post-details">
              <div class="blog-post-details-item blog-post-details-item-left icon-calendar">
                <#if item.date?? && item.date.time??>
                  <p><@fmt.formatDate value=item.date.time type="both" dateStyle="medium" timeStyle="short"/></p>
                </#if>
              </div>
              <div class="blog-post-details-item blog-post-details-item-right">
                <a href="${link}"> Read more<i class="fa fa-chevron-right"></i></a>
              </div>
            </div>
          </div>
        </div>
      </#list>
      <#if cparam.showPagination>
        <#include "../include/pagination.ftl">
      </#if>
    <#elseif editMode>
      <img src="<@hst.link path='/images/essentials/catalog-component-icons/news-list.png'/>"> Click to edit News List
    </#if>
  </div>
</div>