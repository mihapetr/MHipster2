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

describe('Overview e2e test', () => {
  const overviewPageUrl = '/overview';
  const overviewPageUrlPattern = new RegExp('/overview(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const overviewSample = {};

  let overview;
  // let codeStats;
  // let user;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/code-stats',
      body: {"instructions":15740.64,"branches":26216.64,"lines":24815.13,"methods":17396.2,"classes":23056.04},
    }).then(({ body }) => {
      codeStats = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/users',
      body: {"login":"Az@i1K","firstName":"Wyman","lastName":"Lynch","email":"Mireya98@gmail.com","imageUrl":"unusual swat violin"},
    }).then(({ body }) => {
      user = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/overviews+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/overviews').as('postEntityRequest');
    cy.intercept('DELETE', '/api/overviews/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/code-stats', {
      statusCode: 200,
      body: [codeStats],
    });

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
    if (overview) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/overviews/${overview.id}`,
      }).then(() => {
        overview = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (codeStats) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/code-stats/${codeStats.id}`,
      }).then(() => {
        codeStats = undefined;
      });
    }
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

  it('Overviews menu should load Overviews page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('overview');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Overview').should('exist');
    cy.url().should('match', overviewPageUrlPattern);
  });

  describe('Overview page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(overviewPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Overview page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/overview/new$'));
        cy.getEntityCreateUpdateHeading('Overview');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', overviewPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/overviews',
          body: {
            ...overviewSample,
            parent: codeStats,
            user: user,
          },
        }).then(({ body }) => {
          overview = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/overviews+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [overview],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(overviewPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(overviewPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Overview page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('overview');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', overviewPageUrlPattern);
      });

      it('edit button click should load edit Overview page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Overview');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', overviewPageUrlPattern);
      });

      it('edit button click should load edit Overview page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Overview');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', overviewPageUrlPattern);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of Overview', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('overview').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', overviewPageUrlPattern);

        overview = undefined;
      });
    });
  });

  describe('new Overview page', () => {
    beforeEach(() => {
      cy.visit(`${overviewPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Overview');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of Overview', () => {
      cy.get(`[data-cy="dateGenerated"]`).type('2025-06-26T05:18');
      cy.get(`[data-cy="dateGenerated"]`).blur();
      cy.get(`[data-cy="dateGenerated"]`).should('have.value', '2025-06-26T05:18');

      cy.get(`[data-cy="parent"]`).select(1);
      cy.get(`[data-cy="user"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        overview = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', overviewPageUrlPattern);
    });
  });
});
