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

describe('TestReport e2e test', () => {
  const testReportPageUrl = '/test-report';
  const testReportPageUrlPattern = new RegExp('/test-report(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const testReportSample = {};

  let testReport;
  // let featureTst;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/feature-tsts',
      body: {"date":"2025-06-26T10:00:30.018Z"},
    }).then(({ body }) => {
      featureTst = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/test-reports+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/test-reports').as('postEntityRequest');
    cy.intercept('DELETE', '/api/test-reports/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/feature-tsts', {
      statusCode: 200,
      body: [featureTst],
    });

  });
   */

  afterEach(() => {
    if (testReport) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/test-reports/${testReport.id}`,
      }).then(() => {
        testReport = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
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
   */

  it('TestReports menu should load TestReports page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('test-report');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TestReport').should('exist');
    cy.url().should('match', testReportPageUrlPattern);
  });

  describe('TestReport page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(testReportPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TestReport page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/test-report/new$'));
        cy.getEntityCreateUpdateHeading('TestReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', testReportPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/test-reports',
          body: {
            ...testReportSample,
            featureTst: featureTst,
          },
        }).then(({ body }) => {
          testReport = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/test-reports+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [testReport],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(testReportPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(testReportPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details TestReport page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('testReport');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', testReportPageUrlPattern);
      });

      it('edit button click should load edit TestReport page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TestReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', testReportPageUrlPattern);
      });

      it('edit button click should load edit TestReport page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TestReport');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', testReportPageUrlPattern);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of TestReport', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('testReport').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', testReportPageUrlPattern);

        testReport = undefined;
      });
    });
  });

  describe('new TestReport page', () => {
    beforeEach(() => {
      cy.visit(`${testReportPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TestReport');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of TestReport', () => {
      cy.get(`[data-cy="html"]`).type('shocked');
      cy.get(`[data-cy="html"]`).should('have.value', 'shocked');

      cy.get(`[data-cy="runtimeRetention"]`).should('not.be.checked');
      cy.get(`[data-cy="runtimeRetention"]`).click();
      cy.get(`[data-cy="runtimeRetention"]`).should('be.checked');

      cy.get(`[data-cy="missedInstructions"]`).type('17589');
      cy.get(`[data-cy="missedInstructions"]`).should('have.value', '17589');

      cy.get(`[data-cy="instructions"]`).type('3404');
      cy.get(`[data-cy="instructions"]`).should('have.value', '3404');

      cy.get(`[data-cy="missedBranches"]`).type('11477');
      cy.get(`[data-cy="missedBranches"]`).should('have.value', '11477');

      cy.get(`[data-cy="branches"]`).type('31833');
      cy.get(`[data-cy="branches"]`).should('have.value', '31833');

      cy.get(`[data-cy="missedLines"]`).type('8577');
      cy.get(`[data-cy="missedLines"]`).should('have.value', '8577');

      cy.get(`[data-cy="lines"]`).type('18195');
      cy.get(`[data-cy="lines"]`).should('have.value', '18195');

      cy.get(`[data-cy="missedMethods"]`).type('10400');
      cy.get(`[data-cy="missedMethods"]`).should('have.value', '10400');

      cy.get(`[data-cy="methods"]`).type('18750');
      cy.get(`[data-cy="methods"]`).should('have.value', '18750');

      cy.get(`[data-cy="missedClasses"]`).type('4921');
      cy.get(`[data-cy="missedClasses"]`).should('have.value', '4921');

      cy.get(`[data-cy="classes"]`).type('23353');
      cy.get(`[data-cy="classes"]`).should('have.value', '23353');

      cy.get(`[data-cy="featureTst"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        testReport = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', testReportPageUrlPattern);
    });
  });
});
