var tukki = {

  models: {},
  collections: {},
  views: {},
  routers: {},
  
  initialize: function() {
  
    tukki.app = new tukki.routers.Main();
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

tukki.views.Login = Backbone.View.extend({

  events: {
  
    'click [data-id="login"]':      'login',
    'keydown [data-id="username"]': 'keydown',
    'keydown [data-id="password"]': 'keydown'
    
  },

  initialize: function() {
    this.render();
  },
  
  render: function() {
    
    // Display login
    var loginTemplate = $('#login-template').html();
    $(this.el).html(loginTemplate);
    
    // Hide alert
    this.$('[data-id="login-alert"]').hide();
    
    // Show modal
    $(this.el).modal({
    
      backdrop: 'static',
      keyboard: false
    
    });
  },
  
  // Login
  login: function() {
    
    event.preventDefault();
      
    var username = this.$('[data-id="username"]')
                       .val()
                       .trim();
                         
    var password = this.$('[data-id="password"]')
                       .val()
                       .trim();
                       
    var self = this;               
    
    // Send login request
    $.ajax({
      
      type: 'POST',
      url: '/api/login',
      data: JSON.stringify({username: username, password: password}),
      dataType: 'json',
      contentType: 'application/json; charset=utf-8',
      
      // Request succeeded
      success: function(data) {

        // Bad credentials
        if (data.code == 8) {
        
          self.$('.control-group')
              .addClass('error');
          
          self.$('[data-id="login-alert"]')
              .addClass('alert-error')
              .html('<b>Oh snap!</b> ' + data.message)
              .fadeIn();
          
          return;
        }
        
        // Authenticated
        if (data.code == 9) {
          
          $(self.el).modal('hide');
          tukki.app.navigate('/', {trigger: true});
        }
      },
      
      // Request failed
      error: function(error) {
        alert('Error while logging in.');
      }
    });
  },
  
  keydown: function(event) {
  
    // Enter pressed
    if (event.which == 13) {
      this.login();
      return;
    }
  
    // On keydown remove possible error state of inputs
    self.$('.control-group').removeClass('error');
  }

});

tukki.views.ProductList = Backbone.View.extend({

  events: {
  
    'submit #add-product-form': 'addProduct',
    'keydown [data-id="name"]': 'keydown'
  
  },

  initialize: function() {
    this.render();
  },
  
  render: function() {
  
    // Display product list
    var productListTemplate = $('#product-list-template').html();
    $(this.el).html(productListTemplate);
    
    // Hide alerts
    this.$('[data-id="form-alert"]').hide();
    this.$('[data-id="alert"]').hide();
    
    this.renderProducts();
  },
  
  renderProducts: function() {
  
      var alert = this.$('[data-id="alert"]');
      
      // No products
      if (this.collection.length == 0) {
      
        $(alert).addClass('alert-info')
                .html('No products.')
                .fadeIn();
      } else {
        $(alert).fadeOut();
      }
  
      var listElement = this.$('#product-list');
      $(listElement).empty();
    
      // Display each list item
      this.collection.each(function(model) {
        new tukki.views.ProductListItem({el: listElement, model: model})
      });
  },
  
  // Add product
  addProduct: function() {
  
    event.preventDefault();
      
    var productName = this.$('[data-id="name"]')
                          .val()
                          .trim();
    
    var product = new tukki.models.Product();
    
    var self = this;
    
    // Save product
    product.save({name: productName}, {
    
      success: function() {
      
        // Add product to collection
        self.collection.add(product, {at: self.collection.length});
        
        // Empty input for product name
        self.$('[data-id="name"]').val('');
        
        // Fade out possible error alert
        self.$('[data-id="form-alert"]').fadeOut();
      },
      
      error: function(model, response) {
      
        if (response.status == 400) {
          
          var errors = JSON.parse(response.responseText).errors;
          
          if (errors.name) {
          
            // Validation error for name
            if (errors.name.code == 6) {
              self.$('.control-group').addClass('error');
              self.$('[data-id="form-alert"]').addClass('alert-error')
                                              .html('<b>Oh snap!</b> ' + errors.name.message)
                                              .fadeIn();
            }
          }
          
          return;
        }
        
        alert('Error while creating new product: ' + response.status + ' ' + response.statusText);
      }
    });
  },
  
  keydown: function() {
    
    // On keydown remove possible error state of inputs
    self.$('.control-group').removeClass('error'); 
  }
  
});

tukki.views.ProductListItem = Backbone.View.extend({

  initialize: function() {
    this.render();
  },
  
  render: function() {
  
    // Display list item
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
    '/logout': 'logout'
  
  },

  // Login
  login: function() {
    this.renderLogin();
  },
  
  // Logout
  logout: function() {
  
    var self = this;
    
    // Send logout request
    $.get('/api/logout', function() {
      
      $('#content').empty();
      self.navigate('/', {trigger: true});
    });
  },
  
  // Render login
  renderLogin: function() {
    var loginView = new tukki.views.Login({el: $('#modal')})
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
      
        // Invoke authentication
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
    
    var self = this;
    
    product.fetch({
    
      success: function() {
      
        // Inform callback of product
        if (callback) {
          callback(product);
        }
      },
        
      error: function(model, response) {
      
        // Invoke authentication
        if (response.status == 403) {
          self.navigate('#/login', {trigger: true});
          return;
        }
      
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