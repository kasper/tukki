/* Models */
Product = Backbone.Model.extend({urlRoot : 'api/product'});

/* Views */
ProductView = Backbone.View.extend({

  render: function() {
    var template = $('#product-template').html();
    var output = Mustache.render(template, this.model.toJSON());
    $('body').html(output);
  }
});

$(document).ready(function() {

  var product = new Product({id: "1"});
  var productView = new ProductView({model: product});
  product.on('change', function() { productView.render() });
  product.fetch();
});