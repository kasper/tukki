var tukki = {

  models: {},
  collections: {},
  views: {},
  routers: {},
  
  initialize: function() {
  
    new tukki.routers.Main();
    Backbone.history.start();
  }
  
}

/* DOM ready */

$(document).ready(function() {
  tukki.initialize();
});

/* Models */

tukki.models.Product = Backbone.Model.extend({

  urlRoot : 'api/product'
  
});

/* Collections */

tukki.collections.Products = Backbone.Collection.extend({

  model: tukki.models.Product,
  url: 'api/products'
  
});

/* Views */

tukki.views.ProductList = Backbone.View.extend({

  initialize: function() {
    this.render();
  },
  
  render: function() {
  
    // Display product list
    var productListTemplate = $('#product-list-template').html();
    $(this.el).html(productListTemplate);
    
    var self = this;
    
    // Action to save product
    $(this.el).find('#add-product-form').submit(function(event) {
    
      event.preventDefault();
      
      // Save product
      var productName = $(self.el).find('#add-product-form-product-name').val().trim();
      
      if (productName.length < 1) {
        $(self.el).find('#add-product-form-control-group').addClass('error');
        return false;
      } else {
        $(self.el).find('#add-product-form-control-group').removeClass('error');
      }
      
      var product = new tukki.models.Product();
      
      product.save({name: productName}, {
      
        success: function() {
        
          // Add product to collection
          self.collection.add(product, {at: self.collection.length});
        },
        
        error: function(model, response) {
          alert('Error while creating new product: ' + response.status + ' ' + response.statusText);
        } 
      });
    });
    
    this.renderProducts();
  },
  
  renderProducts: function() {
  
    var listElement = $(this.el).find('#product-list');
    $(listElement).empty();
    
    // List products
    this.collection.each(function(model) {
      new tukki.views.ProductListItem({el: listElement, model: model})
    });
  }
  
});

tukki.views.ProductListItem = Backbone.View.extend({

  initialize: function() {
    this.render();
  },
  
  render: function() {
  
    var productListItemTemplate = $('#product-list-item-template').html();
    var output = Mustache.render(productListItemTemplate, this.model.toJSON());
    $(this.el).append(output);
  }
  
});

/* Routers */

tukki.routers.Main = Backbone.Router.extend({

  routes: {
  
    '':             'products',
    '/':            'products',
    '/products':    'products',
    '/product/:id': 'product'
     
  },
  
  // Fetch products
  fetchProducts: function(callback) {
  
    var products = new tukki.collections.Products();
    
    products.fetch({
      
      success: function() {
      
        // Inform callback of products
        if (callback) {
          callback(products);
        }
      },
      
      error: function(response) {
        alert('Error while loading products: ' + response.status + ' ' + response.statusText);
      }
    });
    
    return products;
  },
  
  products: function() {
  
    var self = this;
    
    this.fetchProducts(function(products) {
      self.renderProductList(products);
    });
  },
  
  product: function(id) {
    console.log('Show product with id: ' + id);
  },
  
  // Render products
  renderProductList: function(products) {
  
    var productList = new tukki.views.ProductList({el: $('#content'), collection: products});
    
    products.on("add", function() {
      productList.renderProducts();
    });
  }
  
});