var tukki = {

  models: {},
  collections: {},
  views: {},
  controllers: {},
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

tukki.models.User = Backbone.Model.extend({

  urlRoot: 'api/user'

});

tukki.models.Product = Backbone.Model.extend({

  urlRoot: 'api/product'
  
});

/* Collections */

tukki.collections.Products = Backbone.Collection.extend({

  model: tukki.models.Product,
  url: 'api/products'
  
});

/* Views */

tukki.views.Navigation = Backbone.View.extend({

  initialize: function() {
    this.render();
  },
  
  render: function() {
  
    $('#navigation').find('.nav').show();
    this.$('[data-id="username"]').html(this.model.username);
  }

});

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
    
    $(this.el).undelegate();
    
    // Display login
    var loginTemplate = $('#login-template').html();
    $(this.el).html(loginTemplate);
    
    // Hide alert
    this.$('[data-id="alert"]').hide();
    
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
                       
    var remember = this.$('[data-id="remember"]')
                       .prop('checked'); 
       
    var self = this;            
    
    // Authenticate
    tukki.controllers.Authentication.authenticate({username: username, password: password, remember: remember,
      
      // Authentication succeeded
      authenticated: function(data) {
        
        // Authenticated
        if (data.code == 9) {
          
          $(self.el).modal('hide');
          tukki.app.navigate('/', {trigger: true});
        }
      },
      
      authenticationFailed: function(data) {
        
        // Bad credentials
        if (data.code == 8) {
        
          self.$('[data-id="username-control-group"]').addClass('error');
              
          self.$('[data-id="password-control-group"]').addClass('error');    
          
          self.$('[data-id="alert"]')
              .addClass('alert-error')
              .html('<b>Oh snap!</b> ' + data.message)
              .fadeIn();
          
          return;
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
  
    // Remove possible error state of inputs
    self.$('[data-id="username-control-group"]').removeClass('error');
    self.$('[data-id="password-control-group"]').removeClass('error');
  }

});

tukki.views.Register = Backbone.View.extend({

  events: {
  
    'click [data-id="register"]':   'register',
    'keydown [data-id="username"]': 'keydown',
    'keydown [data-id="password"]': 'keydown',
    'keydown [data-id="email"]':    'keydown'
  
  },

  initialize: function() {
    this.render();
  },
  
  render: function() {
  
    $(this.el).undelegate();
  
    // Display register
    var registerTemplate = $('#register-template').html();
    $(this.el).html(registerTemplate);
    
    // Hide alert
    this.$('[data-id="alert"]').hide();
    
    // Show modal if needed
    $(this.el).modal({
    
      backdrop: 'static',
      keyboard: false
    
    });
  },
  
  register: function() {
  
    event.preventDefault();
  
    var username = this.$('[data-id="username"]')
                       .val()
                       .trim();
                         
    var password = this.$('[data-id="password"]')
                       .val()
                       .trim();
                       
    var email = this.$('[data-id="email"]')
                    .val()
                    .trim();
     
    var user = new tukki.models.User();
                       
    var self = this;
    
    // Save user
    user.save({username: username, password: password, email: email}, {
    
      success: function() {
        
        tukki.controllers.Authentication.authenticate({username: username, password: password, remember: false,
        
          authenticated: function(data) {
            
            // Authenticated
            if (data.code == 9) {
              $(self.el).modal('hide');
              tukki.app.navigate('/', {trigger: true});
            }
            
          },
          
          authenticationFailed: function(data) {
            alert('Authentication failed');
          },
          
          error: function(error) {
            alert('Error while authentication.');
          }
        
        });
      },
      
      error: function(model, response) {
      
        if (response.status == 400) {
          
          var errors = JSON.parse(response.responseText).errors;
          var errorMessages = ['<b>Oh snap!</b>'];
          
          if (errors.username) {
          
            // Validation error for username
            if (errors.username.code == 6) {
              self.$('[data-id="username-control-group"]').addClass('error');
              errorMessages.push(errors.username.message);
            }
          }
          
          if (errors.password) {
            
            // Validation error for password
            if (errors.password.code == 6) {
              self.$('[data-id="password-control-group"]').addClass('error');
              errorMessages.push(errors.password.message);
            }
          }
          
          if (errors.email) {
          
            // Validation error for email
            if (errors.email.code == 6) {
              self.$('[data-id="email-control-group"]').addClass('error');
              errorMessages.push(errors.email.message);
            }
          }
          
          self.$('[data-id="alert"]').addClass('alert-error')
                                     .html(errorMessages.join(' '))
                                     .fadeIn();
          
          return;
        }
        
        alert('Error while creating new user: ' + response.status + ' ' + response.statusText);
      }
    });
  },
  
  keydown: function(event) {
  
    // Enter pressed
    if (event.which == 13) {
      this.register();
      return;
    }
    
    // Remove possible error state of targeted input
    self.$('[data-id="' + event.currentTarget.attributes['data-id'].value + '-control-group"]').removeClass('error');
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
    
    // Remove possible error state of inputs
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
    
    var model = this.model.toJSON();
    model.formattedCreatedOn = new Date(model.createdOn).format('mmmm dS, yyyy');
    
    // Display product
    var productTemplate = $('#product-template').html();
    var output = Mustache.render(productTemplate, model);
    $(this.el).html(output);
  }

});

/* Controllers */

tukki.controllers.Authentication = {

  authenticate: function(authentication) {
  
    var self = this;
  
    $.ajax({
      
      type: 'POST',
      url: '/api/login?remember=' + authentication.remember,
      data: JSON.stringify({username: authentication.username, password: authentication.password}),
      dataType: 'json',
      contentType: 'application/json; charset=utf-8',
      
      // Request succeeded
      success: function(data) {
        
        // Bad credentials
        if (data.code == 8) {
          authentication.authenticationFailed(data);
          return;
        }
      
        authentication.authenticated(data);
      },
      
      // Request failed
      error: function(error) {
        authentication.error(error);
      }
    });
  },
  
  invalidate: function(callback) {
  
    var self = this;
  
    // Logout request
    $.get('/api/logout', function() {
      callback();
    });
  },
  
  user: function(callback) {
    
    var self = this;
  
    $.get('/api/user', function(data) {
      callback(data);
    });
  }
  
}

/* Routers */

tukki.routers.Main = Backbone.Router.extend({

  routes: {
  
    '/login':    'login',
    '/logout':   'logout',
    '/register': 'register'
  
  },

  // Login
  login: function() {
    this.renderLogin();
  },
  
  // Logout
  logout: function() {
  
    var self = this;
    
    // Logout    
    tukki.controllers.Authentication.invalidate(function() {
      self.navigate('/', {trigger: true});
    });
  },
  
  // Register
  register: function() {
    this.renderRegister();
  },
  
  // Render login
  renderLogin: function() {
  
    this.clearViews();
    new tukki.views.Login({el: $('#modal')});
  },
  
  // Render register
  renderRegister: function() {
  
    this.clearViews();
    new tukki.views.Register({el: $('#modal')});
  },
  
  clearViews: function() {
    
    $('#content').empty();
    $('#navigation').find('.nav').hide();
  }
  
});

tukki.routers.Product = Backbone.Router.extend({

  routes: {
  
    '':             'index',
    '/':            'index',
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
  
  index: function() {
    this.products();
  },
  
  // Show products
  products: function() {
    
    var self = this;
    
    $('#navigation li').removeClass('active');
    $('#products-nav-item').addClass('active');
    
    this.fetchProducts(function(products) {
      self.renderNavigation();
      self.renderProductList(products);
    });
  },
  
  // Show product by id
  product: function(id) {
  
    var self = this;
    
    $('#navigation li').removeClass('active');
    $('#products-nav-item').addClass('active');
    
    this.fetchProduct(id, function(product) {
      self.renderNavigation();
      self.renderProduct(product);
    });
  },
  
  // Render navigation
  renderNavigation: function() {
    
    tukki.controllers.Authentication.user(function(user) {
       new tukki.views.Navigation({el: $('#navigation'), model: user});
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
    new tukki.views.Product({el: $('#content'), model: product});
  }
  
});