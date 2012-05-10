var tukki = {

  models: {},
  collections: {},
  views: {},
  controllers: {},
  routers: {},
  
  initialize: function() {
  
    tukki.app = new tukki.routers.Main();
    tukki.product = new tukki.routers.Product();
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

tukki.models.UserStory = Backbone.Model.extend({

  initialize: function(attributes, options) {
    
    this.attributes = attributes;
    this.options = options;
  },

  urlRoot: function() {
    return 'api/product/' + this.options.id + '/story';
  }
  
});

tukki.models.Task = Backbone.Model.extend({

  initialize: function(attributes, options) {
    
    this.attributes = attributes;
    this.options = options;
  },

  urlRoot: function() {
    return 'api/product/' + this.options.id + '/story/' + this.options.index + '/task';
  }
  
});

/* Collections */

tukki.collections.Products = Backbone.Collection.extend({

  model: tukki.models.Product,
  url: 'api/products'
  
});

tukki.collections.UserStories = Backbone.Collection.extend({

  model: tukki.models.UserStory

});

tukki.collections.Tasks = Backbone.Collection.extend({

  model: tukki.models.Task

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
  login: function(event) {
    
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
        console.log('Error while logging in: ' + error + '.');
      }
    });
  },
  
  keydown: function(event) {
  
    // Enter pressed
    if (event.which == 13) {
      this.login(event);
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
  
  register: function(event) {
  
    event.preventDefault();
  
    var username = this.$('[data-id="username"]')
                       .val()
                       .trim();
                         
    var password = this.$('[data-id="password"]')
                       .val()
                       .trim();
                       
    var passwordConfirmation = this.$('[data-id="password-confirmation"]')
                                   .val()
                                   .trim();
                       
    var email = this.$('[data-id="email"]')
                    .val()
                    .trim();
    
    var self = this;
    
    // Passwords do not match
    if (password != passwordConfirmation) {
    
      self.$('[data-id="password-control-group"]').addClass('error');
      
      self.$('[data-id="alert"]').addClass('alert-error')
                                 .html('<b>Oh snap!</b> Passwords do not match.')
                                 .fadeIn();
      return;
    }
     
    var user = new tukki.models.User();
    
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
          
          authenticationFailed: function(error) {
            console.log('Authentication failed: ' + error + '.');
          },
          
          error: function(error) {
            console.log('Error while authentication: ' + error + '.');
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
        
        console.log('Error while creating new user: ' + response.status + ' ' + response.statusText + '.');
      }
    });
  },
  
  keydown: function(event) {
  
    // Enter pressed
    if (event.which == 13) {
      this.register(event);
      return;
    }
    
    // Remove possible error state of targeted input
    self.$('[data-id="' + event.currentTarget.attributes['data-id'].value + '-control-group"]').removeClass('error');
  }

});

tukki.views.DeleteConfirmation = Backbone.View.extend({

  events: {
  
    'click [data-id="cancel"]': 'cancel',
    'click [data-id="delete"]': 'delete'
    
  },

  initialize: function() {
    this.render();
  },
  
  render: function() {
  
    $(this.el).undelegate();
    
    // Display delete confirmation
    var deleteConfirmationTemplate = $('#delete-confirmation-template').html();
    $(this.el).html(deleteConfirmationTemplate);
    
    // Show modal
    $(this.el).modal();
  },
  
  cancel: function(event) {
    
    event.preventDefault();
    $(this.el).modal('hide');
  },
  
  delete: function(event) {
  
    event.preventDefault();
    this.options.destroy();
  }

});

tukki.views.NewUserStory = Backbone.View.extend({

  events: {
  
    'click [data-id="add"]':        'addUserStory',
    'click [data-id="cancel"]':     'cancel',
    'keydown [data-id="scenario"]': 'keydown',
    'keydown [data-id="given"]':    'keydown',
    'keydown [data-id="when"]':     'keydown',
    'keydown [data-id="then"]':     'keydown'
    
  },

  initialize: function() {
    this.render();
  },
  
  render: function() {
  
    $(this.el).undelegate();
    
    // Display template
    var newUserStoryTemplate = $('#new-user-story-template').html();
    $(this.el).html(newUserStoryTemplate);
    
    // Hide alert
    this.$('[data-id="alert"]').hide();
    
    // Show modal
    $(this.el).modal();
  },
  
  addUserStory: function(event) {
  
    event.preventDefault();
    
    var scenario = this.$('[data-id="scenario"]')
                       .val()
                       .trim();
                       
    var given = this.$('[data-id="given"]')
                    .val()
                    .trim();
                    
    var when = this.$('[data-id="when"]')
                   .val()
                   .trim();
                       
    var then = this.$('[data-id="then"]')
                   .val()
                   .trim();
                             
    var story = new tukki.models.UserStory({}, {id: this.model.id});
    
    var self = this;
    
    // Save story
    story.save({scenario: scenario, given: given, when: when, then: then}, {
    
      success: function() {
      
        // Add story to collection
        self.collection.add(story, {at: self.collection.length});
      
        $(self.el).modal('hide');
      },
      
      error: function(model, response) {
      
        if (response.status == 400) {
          
          var errors = JSON.parse(response.responseText).errors;
          var errorMessages = ['<b>Oh snap!</b>'];
          
          if (errors.scenario) {
          
            // Validation error for scenario
            if (errors.scenario.code == 6) {
              self.$('[data-id="scenario-control-group"]').addClass('error');
              errorMessages.push(errors.scenario.message);
            }
          }
          
          if (errors.given) {
            
            // Validation error for given
            if (errors.given.code == 6) {
              self.$('[data-id="given-control-group"]').addClass('error');
              errorMessages.push(errors.given.message);
            }
          }
          
          if (errors.when) {
          
            // Validation error for when
            if (errors.when.code == 6) {
              self.$('[data-id="when-control-group"]').addClass('error');
              errorMessages.push(errors.when.message);
            }
          }
          
          if (errors.then) {
          
            // Validation error for then
            if (errors.then.code == 6) {
              self.$('[data-id="then-control-group"]').addClass('error');
              errorMessages.push(errors.then.message);
            }
          }
          
          self.$('[data-id="alert"]').addClass('alert-error')
                                     .html(errorMessages.join(' '))
                                     .fadeIn();
          
          return;
        }
        
        console.log('Error while creating new story: ' + response.status + ' ' + response.statusText + '.');
      }
    });
  },
  
  cancel: function(event) {
    
    event.preventDefault();
    $(this.el).modal('hide');
  },
  
  keydown: function(event) {
  
    // Enter pressed
    if (event.which == 13) {
      this.addUserStory(event);
      return;
    }
    
    // Remove possible error state of targeted input
    self.$('[data-id="' + event.currentTarget.attributes['data-id'].value + '-control-group"]').removeClass('error');
  }
  
});

tukki.views.NewTask = Backbone.View.extend({

  events: {
  
    'click [data-id="add"]':           'addTask',
    'click [data-id="cancel"]':        'cancel',
    'keydown [data-id="description"]': 'keydown',
    
  },

  initialize: function() {
    this.render();
  },
  
  render: function() {
  
    $(this.el).undelegate();
    
    // Display template
    var newTaskTemplate = $('#new-task-template').html();
    $(this.el).html(newTaskTemplate);
    
    // Hide alert
    this.$('[data-id="alert"]').hide();
    
    // Show modal
    $(this.el).modal();
  },
  
  addTask: function(event) {
  
    event.preventDefault();
    
    var description = this.$('[data-id="description"]')
                          .val()
                          .trim();
                             
    var task = new tukki.models.Task({}, {id: this.model.id, index: this.options.index});
    
    var self = this;
    
    // Save task
    task.save({description: description}, {
    
      success: function() {
      
        // Add task to collection
        self.collection.add(task, {at: self.collection.length});
      
        $(self.el).modal('hide');
      },
      
      error: function(model, response) {
      
        if (response.status == 400) {
          
          var errors = JSON.parse(response.responseText).errors;
          var errorMessages = ['<b>Oh snap!</b>'];
          
          if (errors.description) {
          
            // Validation error for description
            if (errors.description.code == 6) {
              self.$('[data-id="description-control-group"]').addClass('error');
              errorMessages.push(errors.description.message);
            }
          }
                    
          self.$('[data-id="alert"]').addClass('alert-error')
                                     .html(errorMessages.join(' '))
                                     .fadeIn();
          
          return;
        }
        
        console.log('Error while creating new task: ' + response.status + ' ' + response.statusText + '.');
      }
    });
  },
  
  cancel: function(event) {
    
    event.preventDefault();
    $(this.el).modal('hide');
  },
  
  keydown: function(event) {
  
    // Enter pressed
    if (event.which == 13) {
      this.addTask(event);
      return;
    }
    
    // Remove possible error state of targeted input
    self.$('[data-id="' + event.currentTarget.attributes['data-id'].value + '-control-group"]').removeClass('error');
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

tukki.views.ProductList = Backbone.View.extend({

  events: {
  
    'submit #add-product-form': 'addProduct',
    'keydown [data-id="name"]': 'keydown'
  
  },

  initialize: function() {
    this.render();
  },
  
  render: function() {
  
    $(this.el).undelegate();
  
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
  addProduct: function(event) {
  
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
        
        console.log('Error while creating new product: ' + response.status + ' ' + response.statusText + '.');
      }
    });
  },
  
  keydown: function() {
    
    // Remove possible error state of inputs
    self.$('.control-group').removeClass('error'); 
  }
  
});

tukki.views.UserStoryTableItem = Backbone.View.extend({

  initialize: function() {
    this.render();
  },
  
  render: function() {
  
    var model = this.model.toJSON();
    model.productId = this.model.options.productId;
    model.index = this.model.options.index;
  
    // Status
    var status = this.$('[status-index="' + model.taskIndex + '"]');
    var statusClass = ['label-info', 'label-warning', 'label-success'];
    model.status.class = (statusClass[this.model.attributes.status.code - 1]);
  
    // Display list item
    var userStoryTableItemTemplate = $('#user-story-table-item-template').html();
    var output = Mustache.render(userStoryTableItemTemplate, model);
    $(this.el).append(output);
  }
  
});

tukki.views.Product = Backbone.View.extend({

  events: {
  
    'click [data-id="new-user-story"]': 'newUserStory',
    'click [data-id="delete"]':         'delete'
    
  },

  initialize: function() {
    this.render();
  },
  
  render: function() {
  
    $(this.el).undelegate();
    
    var model = this.model.toJSON();
    
    model.formattedWhenAdded = new Date(model.whenAdded).format('mmmm dS, yyyy');
    
    // Display product
    var productTemplate = $('#product-template').html();
    var output = Mustache.render(productTemplate, model);
    $(this.el).html(output);
    
    // Hide alert
    this.$('[data-id="alert"]').hide();
    
    // Hide delete button
    this.$('[data-id="delete"]').hide();
    
    var self = this;
    
    tukki.controllers.Authentication.user(function(authenticationUser) {
      
      // Only the product owner can delete the product and sort user stories
      if (authenticationUser.username == model.productOwner.username) {
        self.$('[data-id="delete"]').show();
        self.sortable();
      }
    });
    
    this.renderUserStories();
  },
  
  renderUserStories: function() {
  
    var alert = this.$('[data-id="alert"]');
      
    // No stories
    if (this.collection.length == 0) {
      
      $(alert).addClass('alert-info')
              .html('No user stories.')
              .fadeIn();
              
      $('#user-story-table').hide();
    } else {
      $(alert).fadeOut();
      $('#user-story-table').show();
    }
  
    var tableBodyElement = this.$('#user-story-table tbody');
    $(tableBodyElement).empty();
    
    var self = this;
    
    // Display each table item
    this.collection.each(function(model, index) {
      model.options.productId = self.model.id; 
      model.options.index = index + 1;
      new tukki.views.UserStoryTableItem({el: tableBodyElement, model: model})
    });
  },
  
  delete: function(event) {
    
    event.preventDefault();
    
    var self = this;
    
    new tukki.views.DeleteConfirmation({el: $('#modal'),
    
      destroy: function() {
        
        var view = this;
        
        // Delete product
        self.model.destroy({
    
          success: function(data) {
      
            $(view.el).modal('hide');
            tukki.app.navigate('/', {trigger: true});
          },
      
          error: function(data) {    
            console.log('Error while deleting product.');
          }
        });
      }
    });
  },
  
  newUserStory: function(event) {
    
    event.preventDefault();
    new tukki.views.NewUserStory({el: $('#modal'), model: this.model, collection: this.collection});
  },
  
  sortable: function() {
    
    // Preserve width of cells
    var helper = function(event, ui) {
      
      ui.children().each(function() {
		    $(this).width($(this).width());
      });
	   
	   return ui;
    };
    
    var self = this;
    
    // Sortable
    this.$('#user-story-table tbody').sortable({helper: helper,
    
      start: function(event, ui) {
        ui.originalPosition.rowIndex = ui.item.context.rowIndex;
      },
    
      stop: function(event, ui) {
      
        var from = ui.originalPosition.rowIndex - 1;
        var to = ui.item.context.rowIndex - 1;
        
        // Position did not change
        if (to == from) {
          return;
        }
        
        // Rearrenge collection
        var userStory = self.options.collection.at(from);
        self.options.collection.remove(userStory);
        self.options.collection.add(userStory, {at: to});
        
        // Prioritise
        $.ajax({
        
          url: '/api/product/' + self.model.id + '/story/' + from + '/to/' + to, 
          type: 'PUT'
        });
      }
    });
  }

});

tukki.views.TaskTableItem = Backbone.View.extend({

  initialize: function() {
    this.render();
  },
  
  render: function() {
  
    var model = this.model.toJSON();
    model.productId = this.model.options.productId;
    model.userStoryIndex = this.model.options.userStoryIndex;
    model.taskIndex = this.model.options.taskIndex;
  
    // Status
    var status = this.$('[status-index="' + model.taskIndex + '"]');
    var statusClass = ['label-info', 'label-warning', 'label-success'];
    model.status.class = (statusClass[this.model.attributes.status.code - 1]);
  
    // Display list item
    var taskTableItemTemplate = $('#task-table-item-template').html();
    var output = Mustache.render(taskTableItemTemplate, model);
    $(this.el).append(output);
    
    if (model.implementer != null) {
    
      this.$('[data-id="toggle-implementer"]').filter('[data-index="' + model.taskIndex + '"]').hide();
      this.$('[data-id="toggle-done"]').filter('[data-index="' + model.taskIndex + '"]').hide();
    
      var self = this;
      
      tukki.controllers.Authentication.user(function(authenticationUser) {
        
        // Only the implementer can toggle the task and mark it as done
        if (authenticationUser.username == model.implementer.username) {
          this.$('[data-id="toggle-implementer"]').filter('[data-index="' + model.taskIndex + '"]').show();
          this.$('[data-id="toggle-done"]').filter('[data-index="' + model.taskIndex + '"]').show();
        }
      });
    }
  }
  
});

tukki.views.UserStory = Backbone.View.extend({

  events: {
  
    'click [data-id="new-task"]':           'newTask',
    'click [data-id="delete"]':             'delete',
    'click [data-id="toggle-implementer"]': 'toggleImplementer',
    'click [data-id="toggle-done"]':        'toggleDone',
    'click [data-id="remove-task"]':        'removeTask'
    
  },

  initialize: function() {
    this.render();
  },
  
  render: function() {
  
    $(this.el).undelegate();
    
    var model = this.model.attributes.stories[this.options.index - 1];
    
    model.formattedWhenCreated = new Date(model.whenCreated).format('mmmm dS, yyyy');
    
    // Display user story
    var productTemplate = $('#user-story-template').html();
    var output = Mustache.render(productTemplate, model);
    $(this.el).html(output);
    
    // Hide alert
    this.$('[data-id="alert"]').hide();
    
    this.renderTasks();
  },
  
  renderTasks: function() {
  
    var alert = this.$('[data-id="alert"]');
      
    // No tasks
    if (this.collection.length == 0) {
      
      $(alert).addClass('alert-info')
              .html('No tasks.')
              .fadeIn();
              
      $('#task-table').hide();
    } else {
      $(alert).fadeOut();
      $('#task-table').show();
    }
    
    var tableBodyElement = this.$('#task-table tbody');
    $(tableBodyElement).empty();
    
    var self = this;
    
    // Display each table item
    this.collection.each(function(model, index) {
      model.options.productId = self.model.id;
      model.options.userStoryIndex = self.options.index;
      model.options.taskIndex = index + 1;
      new tukki.views.TaskTableItem({el: tableBodyElement, model: model})
    });
  },
  
  newTask: function(event) {
    
    event.preventDefault();
    new tukki.views.NewTask({el: $('#modal'), model: this.model, index: this.options.index - 1, collection: this.collection});
  },
  
  toggleImplementer: function(event) {
  
    event.preventDefault();
        
    var storyIndex = (this.options.index - 1);
    var taskIndex = $(event.target.parentNode).data('index') - 1;
    
    var self = this;
    
    // Toggle implementer
    $.ajax({
      
      url: '/api/product/' + self.model.id + '/story/' + storyIndex + '/task/' + taskIndex + '/implementer', 
      type: 'PUT',
    
      success: function(data) {
        tukki.product.userStory(self.model.id, storyIndex + 1);
      },
    
      error: function(data) {  
        console.log('Error while toggling implementer.');
      }
    });
  },
  
  toggleDone: function(event) {
  
    event.preventDefault();
    
    var storyIndex = (this.options.index - 1);
    var taskIndex = $(event.target.parentNode).data('index') - 1;
    
    var self = this;
    
    // Toggle done
    $.ajax({
      
      url: '/api/product/' + self.model.id + '/story/' + storyIndex + '/task/' + taskIndex + '/done', 
      type: 'PUT',
    
      success: function(data) {
        tukki.product.userStory(self.model.id, storyIndex + 1);
      },
    
      error: function(data) {  
        console.log('Error while toggling done.');
      }
    });
  },
  
  delete: function(event) {
    
    event.preventDefault();
    
    var self = this;
    
    new tukki.views.DeleteConfirmation({el: $('#modal'),
    
    destroy: function() {
        
        var view = this;
        
        // Delete user story        
        $.ajax({
          
          url: '/api/product/' + self.model.id + '/story/' + (self.options.index - 1), 
          type: 'DELETE',
    
          success: function(data) {
      
            $(view.el).modal('hide');
            tukki.app.navigate('#/product/' + self.model.id, {trigger: true});
          },
      
          error: function(data) {  
            console.log('Error while deleting user story.');
          }
        });
      }
    });
  },
  
  removeTask: function(event) {
    
    event.preventDefault();
    
    var storyIndex = (this.options.index - 1);
    var taskIndex = $(event.target.parentNode).data('index') - 1;
    
    var self = this;
    
    new tukki.views.DeleteConfirmation({el: $('#modal'),
    
    destroy: function() {
        
        var view = this;
        
        // Delete task
        $.ajax({
          
          url: '/api/product/' + self.model.id + '/story/' + storyIndex + '/task/' + taskIndex, 
          type: 'DELETE',
    
          success: function(data) {
      
            // Remove from collection
            var task = self.collection.at(taskIndex);
            self.collection.remove(task);
      
            $(view.el).modal('hide');
          },
      
          error: function(data) {  
            console.log('Error while removing task.');
          }
        });
      }
    });
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
  
  user: function(callback, error) {
  
    $.get('/api/user', function(data) {
      callback(data);
    }).error(function(data) {
      error(data);
    });
  },
  
  authenticated: function(callback) {
  
    this.user(
    
    function(data) {
      callback(true);
    },
    
    function(error) {
      callback(false);
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
  
    var self = this;
  
    tukki.controllers.Authentication.authenticated(function(authenticated) {
    
      if (authenticated) {
        self.navigate('/', {trigger: true});
        return;
      }
    
      self.renderLogin();
    });
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
  
    var self = this;
  
    tukki.controllers.Authentication.authenticated(function(authenticated) {
    
      if (authenticated) {
        self.navigate('/', {trigger: true});
        return;
      }
    
      self.renderRegister();
    });
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
  
    '':                                 'index',
    '/':                                'index',
    '/products':                        'products',
    '/product/:id':                     'product',
    '/product/:productId/story/:index': 'userStory'
     
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
        
        console.log('Error while loading products: ' + response.status + ' ' + response.statusText + '.');
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
      
        console.log('Error while loading product: ' + response.status + ' ' + response.statusText + '.');
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
  
  // Show story
  userStory: function(productId, index) {
  
    var self = this;
  
    this.fetchProduct(productId, function(product) {
      self.renderNavigation();
      self.renderUserStory(product, index);
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
    
    var stories = new tukki.collections.UserStories(product.attributes.stories);
    var productView = new tukki.views.Product({el: $('#content'), model: product, collection: stories});
    
    stories.on("add", function() {
      productView.renderUserStories();
    });
    
    stories.on("delete", function() {
      productView.renderUserStories();
    });
  },
  
  // Render user story
  renderUserStory: function(product, index) {

    var tasks = new tukki.collections.Tasks(product.attributes.stories[index - 1].tasks);
    var userStoryView = new tukki.views.UserStory({el: $('#content'), model: product, index: index, collection: tasks});
    
    tasks.on("add", function() {
      userStoryView.renderTasks();
    });
    
    tasks.on("remove", function() {
      userStoryView.renderTasks();
    });
  }
  
});