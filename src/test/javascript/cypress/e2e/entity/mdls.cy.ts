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

describe('MDLS e2e test', () => {
  const mDLSPageUrl = '/mdls';
  const mDLSPageUrlPattern = new RegExp('/mdls(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const mDLSSample = {};

  let mDLS;
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
      body: {"login":"nWak","firstName":"Rylan","lastName":"Jaskolski","email":"Leo69@yahoo.com","imageUrl":"scared swathe platypus"},
    }).then(({ body }) => {
      user = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/mdls+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/mdls').as('postEntityRequest');
    cy.intercept('DELETE', '/api/mdls/*').as('deleteEntityRequest');
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

  });
   */

  afterEach(() => {
    if (mDLS) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/mdls/${mDLS.id}`,
      }).then(() => {
        mDLS = undefined;
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

  it('MDLS menu should load MDLS page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('mdls');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('MDLS').should('exist');
    cy.url().should('match', mDLSPageUrlPattern);
  });

  describe('MDLS page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(mDLSPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create MDLS page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/mdls/new$'));
        cy.getEntityCreateUpdateHeading('MDLS');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', mDLSPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/mdls',
          body: {
            ...mDLSSample,
            user: user,
          },
        }).then(({ body }) => {
          mDLS = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/mdls+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [mDLS],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(mDLSPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(mDLSPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details MDLS page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('mDLS');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', mDLSPageUrlPattern);
      });

      it('edit button click should load edit MDLS page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MDLS');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', mDLSPageUrlPattern);
      });

      it('edit button click should load edit MDLS page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MDLS');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', mDLSPageUrlPattern);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of MDLS', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('mDLS').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', mDLSPageUrlPattern);

        mDLS = undefined;
      });
    });
  });

  describe('new MDLS page', () => {
    beforeEach(() => {
      cy.visit(`${mDLSPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('MDLS');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of MDLS', () => {
      cy.get(`[data-cy="baseConfig"]`).type('sun whose');
      cy.get(`[data-cy="baseConfig"]`).should('have.value', 'sun whose');

      cy.get(`[data-cy="content"]`).type('aha promptly unearth');
      cy.get(`[data-cy="content"]`).should('have.value', 'aha promptly unearth');

      cy.get(`[data-cy="user"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        mDLS = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', mDLSPageUrlPattern);
    });
  });
});
