import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('Feature e2e test', () => {
  const featurePageUrl = '/feature';
  const featurePageUrlPattern = new RegExp('/feature(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const featureSample = {};

  let feature;
  // let user;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/users',
      body: {"login":"jxJaL","firstName":"Macy","lastName":"Jacobi","email":"Marilie28@hotmail.com","imageUrl":"blah"},
    }).then(({ body }) => {
      user = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/features+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/features').as('postEntityRequest');
    cy.intercept('DELETE', '/api/features/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [user],
    });

    cy.intercept('GET', '/api/projects', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/feature-tsts', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (feature) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/features/${feature.id}`,
      }).then(() => {
        feature = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (user) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/users/${user.id}`,
      }).then(() => {
        user = undefined;
      });
    }
  });
   */

  it('Features menu should load Features page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('feature');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Feature').should('exist');
    cy.url().should('match', featurePageUrlPattern);
  });

  describe('Feature page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(featurePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Feature page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/feature/new$'));
        cy.getEntityCreateUpdateHeading('Feature');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', featurePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/features',
          body: {
            ...featureSample,
            user: user,
          },
        }).then(({ body }) => {
          feature = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/features+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [feature],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(featurePageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(featurePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Feature page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('feature');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', featurePageUrlPattern);
      });

      it('edit button click should load edit Feature page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Feature');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', featurePageUrlPattern);
      });

      it('edit button click should load edit Feature page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Feature');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', featurePageUrlPattern);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of Feature', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('feature').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', featurePageUrlPattern);

        feature = undefined;
      });
    });
  });

  describe('new Feature page', () => {
    beforeEach(() => {
      cy.visit(`${featurePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Feature');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of Feature', () => {
      cy.get(`[data-cy="name"]`).type('fine apropos violently');
      cy.get(`[data-cy="name"]`).should('have.value', 'fine apropos violently');

      cy.get(`[data-cy="content"]`).type('although the ah');
      cy.get(`[data-cy="content"]`).should('have.value', 'although the ah');

      cy.get(`[data-cy="user"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        feature = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', featurePageUrlPattern);
    });
  });
});
