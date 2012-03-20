var tukki = {

  models: {},
  collections: {},
  views: {},
  routers: {},
  
  init: function() {
  
    new tukki.routers.Main();
    Backbone.history.start();
  }
  
}

/* Models */
 
tukki.models.Product = Backbone.Model.extend({

  urlRoot : 'api/product'
  
});

/* Collections */

tukki.collections.Products = Backbone.Model.extend({
  
  model: tukki.models.Product, 
  url: 'api/products'
  
});

/* Views */

tukki.views.ProductView = Backbone.View.extend({

  render: function() {
  
    var template = $('#product-template').html();
    var output = Mustache.render(template, this.model.toJSON());
    $('#content').html(output);
  }
  
});

/* Routers */

tukki.routers.Main = Backbone.Router.extend({

  routes: {
  
    '':             'index',
    '/':            'index',
    '/products':    'products',
    '/product/:id': 'product'
    
  },
  
  index: function() {
  
    console.log('Index');
  },
  
  products: function() {
  
    console.log('List products');
  },
  
  product: function(id) {
  
    console.log('Show product ' + id);
  }
  
});

$(document).ready(function() {

  tukki.init();

  var product = new tukki.models.Product({id: '4f6877940364a650f6c8930c', name: 'Hello World!'});
  var productView = new tukki.views.ProductView({model: product});
  product.on('change', function() { productView.render() });
  //product.fetch();
  productView.render();
  
});