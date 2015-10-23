<#include "../include/imports.ftl">
<div class="col-md-9 col-sm-9">
  <#if pageable??>
    <div class="isotope" id="masonry-elements">
      <#list pageable.items as item>
        <@hst.link var="link" hippobean=item/>
        <div class="feature blog-masonry isotope-item">
          <#if item.images?? && item.images[0].smallsquare??>
            <@hst.link var="img" hippobean=item.images[0].smallsquare />
            <div class="feature-image img-overlay">
              <img src="${img}" alt="" />
              <div class="item-img-overlay">
                <div class="item_img_overlay_link">
                  <a href="${link}" title="${item.title?html}"> </a>
                </div>
                <div class="item_img_overlay_content">
                  <h3 class="thumb-label-item-title">
                    <a href="${link}">Add to cart</a>
                  </h3>
                </div>
              </div>
            </div>
          </#if>
          <div class="feature-content">
            <h3 class="h3-body-title blog-title">
              <a href="${link}">${item.title?html}</a>
            </h3>
            <p>${item.introduction?html}</p>
          </div>
          <div class="feature-details">
            <i class="icon-banknote"> </i> <span><@fmt.formatNumber value="${item.price}" type="currency" /></span> 
            <#--<div class="feature-share"><span data-score="3.5" class="product-rating"/></div>-->
          </div>
        </div>
      </#list>
    </div>
    <#if cparam.showPagination>
      <#include "../include/pagination.ftl">
    </#if>
  </#if>
</div>
 
<@hst.headContribution category="htmlHead">
  <link rel="stylesheet" href="<@hst.webfile path="/css/jquery.raty.css"/>" />
</@hst.headContribution>
<@hst.headContribution category="htmlBodyEnd">
    <script type="text/javascript" src="<@hst.webfile path="/js/jquery-2.1.0.min.js"/>"></script>
</@hst.headContribution>
<@hst.headContribution category="htmlBodyEnd">
  <script type="text/javascript" src="<@hst.webfile path="/js/jquery.raty.js"/>"></script>
</@hst.headContribution>
<@hst.headContribution category="htmlBodyEnd">
  <script type="text/javascript" src="<@hst.webfile path="/js/jquery.isotope.js"/>"></script>
</@hst.headContribution>
<@hst.headContribution category="htmlBodyEnd">
  <script type="text/javascript">
    jQuery(document).ready(function($) {
   
      $(window).resize(function() {
          $('#masonry-elements,.portfolio-items').isotope('reLayout');
      });
   
      var $cont = $('.portfolio-items');
   
      $cont.isotope({
          itemSelector: '.portfolio-items .thumb-label-item',
          masonry: {columnWidth: $('.isotope-item:first').width(), gutterWidth: 6},
          filter: '*',
          transformsEnabled: false,
          layoutMode: 'masonry'
      });
 
    });
 
    $(window).load(function() {
      var $masonryElement = $('#masonry-elements');
      $masonryElement.isotope({
        transformsEnabled: false,
        masonry: {
          columnWidth: 235,
          gutterWidth: 15
        }
      });
      $('#masonry-elements,.portfolio-items').isotope('reLayout');
    });  
 
    $('#masonry-elements .product-rating').raty({
        score: function() {
            return $(this).attr('data-score');
        },
        readOnly: true,
        half: true,
        starType :  'i'
    });
  </script>
</@hst.headContribution>