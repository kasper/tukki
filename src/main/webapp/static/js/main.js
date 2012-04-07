var tukki = {

  models: {},
  collections: {},
  views: {},
  routers: {},
  
  initialize: function() {
  
    new tukki.routers.Main();
    new tukki.routers.Product();
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
  
    $(this.el).empty();
  
    // Display product list
    var productListTemplate = $('#product-list-template').html();
    $(this.el).html(productListTemplate);
    
    // Hide alert
    $(this.el).find('#product-list-alert').hide();
    
    var self = this;
    
    // Add product form 
    $(this.el).find('#add-product-form').submit(function(event) {
    
      event.preventDefault();
      
      var productName = $(self.el).find('[data-id="name"]').val().trim();
      
      // Empty product name
      if (productName.length < 1) {
        $(self.el).find('.control-group').addClass('error');
        return false;
      }
      
      var product = new tukki.models.Product();
      
      // Save product
      product.save({name: productName}, {
      
        success: function() {
        
          // Add product to collection
          self.collection.add(product, {at: self.collection.length});
          
          // Empty input for product name
          $(self.el).find('#add-product-form-product-name').val('');
        },
        
        error: function(model, response) {
          alert('Error while creating new product: ' + response.status + ' ' + response.statusText);
        }
      });
    });
    
    // On keydown remove possible error alert
    $(this.el).find('#add-product-form-product-name').keydown(function() {
      $(self.el).find('.control-group').removeClass('error');
    });
    
    this.renderProducts();
  },
  
  renderProducts: function() {
  
      var alert = $(this.el).find('#product-list-alert');
      
      // No products
      if (this.collection.length == 0) {
        $(alert).fadeIn().addClass('alert-info').html('No products.');
      } else {
        $(alert).fadeOut();
      }
  
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

tukki.views.Product = Backbone.View.extend({

  initialize: function() {
    this.render();
  },
  
  render: function() {
  
    $(this.el).empty();
    
    // Display product
    var productTemplate = $('#product-template').html();
    var output = Mustache.render(productTemplate, this.model.toJSON());
    $(this.el).html(output);
  }

});

/* Routers */

tukki.routers.Main = Backbone.Router.extend({

  routes: {
  
    '/login': 'login',
  
  },

  login: function() {

    $('#login-modal').modal({
      backdrop: 'static',
      keyboard: false
    });
  }
  
});

tukki.routers.Product = Backbone.Router.extend({

  routes: {
  
    '':             'products',
    '/':            'products',
    '/products':    'products',
    '/product/:id': 'product'
     
  },
  
  // Fetch products
  fetchProducts: function(callback) {
  
    var products = new tukki.collections.Products();
    
    var self = this;
    
    products.fetch({
      
      success: function() {
      
        // Inform callback of products
        if (callback) {
          callback(products);
        }
      },
      
      error: function(model, response) {
      
        // Authenticate
        if (response.status == 403) {
          self.navigate('#/login', {trigger: true});
          return;
        }
        
        alert('Error while loading products: ' + response.status + ' ' + response.statusText);
      }
    });
    
    return products;
  },
  
  // Fetch product by id
  fetchProduct: function(productId, callback) {
  
    var product = new tukki.models.Product({id: productId});
    
    product.fetch({
    
      success: function() {
      
        // Inform callback of product
        if (callback) {
          callback(product);
        }
      },
        
      error: function(model, response) {
        alert('Error while loading product: ' + response.status + ' ' + response.statusText);
      }
    });
    
    return product;
  },
  
  // Show products
  products: function() {
  
    var self = this;
    
    this.fetchProducts(function(products) {
      self.renderProductList(products);
    });
  },
  
  // Show product by id
  product: function(id) {
  
    var self = this;
    
    this.fetchProduct(id, function(product) {
      self.renderProduct(product);
    });
  },
  
  // Render products
  renderProductList: function(products) {
  
    var productListView = new tukki.views.ProductList({el: $('#content'), collection: products});
    
    products.on("add", function() {
      productListView.renderProducts();
    });
  },
  
  // Render product
  renderProduct: function(product) {
    var productView = new tukki.views.Product({el: $('#content'), model: product});
  }
  
});