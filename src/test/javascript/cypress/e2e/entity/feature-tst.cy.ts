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

describe('FeatureTst e2e test', () => {
  const featureTstPageUrl = '/feature-tst';
  const featureTstPageUrlPattern = new RegExp('/feature-tst(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const featureTstSample = {};

  let featureTst;
  // let codeStats;
  // let project;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/code-stats',
      body: {"instructions":4301.88,"branches":7184.76,"lines":6874.21,"methods":25615.22,"classes":9708.85},
    }).then(({ body }) => {
      codeStats = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/projects',
      body: {"name":"fluffy","description":"modulo settler transplant","creationTimeStamp":"2025-06-25T15:58:53.727Z","location":"ew elderly during"},
    }).then(({ body }) => {
      project = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/feature-tsts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/feature-tsts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/feature-tsts/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/code-stats', {
      statusCode: 200,
      body: [codeStats],
    });

    cy.intercept('GET', '/api/test-reports', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/features', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/projects', {
      statusCode: 200,
      body: [project],
    });

  });
   */

  afterEach(() => {
    if (featureTst) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/feature-tsts/${featureTst.id}`,
      }).then(() => {
        featureTst = undefined;
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
    if (project) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/projects/${project.id}`,
      }).then(() => {
        project = undefined;
      });
    }
  });
   */

  it('FeatureTsts menu should load FeatureTsts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('feature-tst');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FeatureTst').should('exist');
    cy.url().should('match', featureTstPageUrlPattern);
  });

  describe('FeatureTst page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(featureTstPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FeatureTst page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/feature-tst/new$'));
        cy.getEntityCreateUpdateHeading('FeatureTst');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', featureTstPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/feature-tsts',
          body: {
            ...featureTstSample,
            parent: codeStats,
            project: project,
          },
        }).then(({ body }) => {
          featureTst = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/feature-tsts+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [featureTst],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(featureTstPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(featureTstPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details FeatureTst page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('featureTst');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', featureTstPageUrlPattern);
      });

      it('edit button click should load edit FeatureTst page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FeatureTst');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', featureTstPageUrlPattern);
      });

      it('edit button click should load edit FeatureTst page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FeatureTst');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', featureTstPageUrlPattern);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of FeatureTst', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('featureTst').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', featureTstPageUrlPattern);

        featureTst = undefined;
      });
    });
  });

  describe('new FeatureTst page', () => {
    beforeEach(() => {
      cy.visit(`${featureTstPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FeatureTst');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of FeatureTst', () => {
      cy.get(`[data-cy="date"]`).type('2025-06-26T07:24');
      cy.get(`[data-cy="date"]`).blur();
      cy.get(`[data-cy="date"]`).should('have.value', '2025-06-26T07:24');

      cy.get(`[data-cy="parent"]`).select(1);
      cy.get(`[data-cy="project"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        featureTst = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', featureTstPageUrlPattern);
    });
  });
});
