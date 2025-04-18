import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './feedback.reducer';

export const Feedback = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const feedbackList = useAppSelector(state => state.feedback.entities);
  const loading = useAppSelector(state => state.feedback.loading);
  const totalItems = useAppSelector(state => state.feedback.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="feedback-heading" data-cy="FeedbackHeading">
        <Translate contentKey="smartVaxApp.feedback.home.title">Feedbacks</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="smartVaxApp.feedback.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/feedback/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="smartVaxApp.feedback.home.createLabel">Create new Feedback</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {feedbackList && feedbackList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="smartVaxApp.feedback.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('messageText')}>
                  <Translate contentKey="smartVaxApp.feedback.messageText">Message Text</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('messageText')} />
                </th>
                <th className="hand" onClick={sort('sideEffects')}>
                  <Translate contentKey="smartVaxApp.feedback.sideEffects">Side Effects</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('sideEffects')} />
                </th>
                <th className="hand" onClick={sort('treatment')}>
                  <Translate contentKey="smartVaxApp.feedback.treatment">Treatment</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('treatment')} />
                </th>
                <th className="hand" onClick={sort('dateSubmitted')}>
                  <Translate contentKey="smartVaxApp.feedback.dateSubmitted">Date Submitted</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dateSubmitted')} />
                </th>
                <th>
                  <Translate contentKey="smartVaxApp.feedback.parent">Parent</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="smartVaxApp.feedback.vaccination">Vaccination</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {feedbackList.map((feedback, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/feedback/${feedback.id}`} color="link" size="sm">
                      {feedback.id}
                    </Button>
                  </td>
                  <td>{feedback.messageText}</td>
                  <td>{feedback.sideEffects}</td>
                  <td>{feedback.treatment}</td>
                  <td>
                    {feedback.dateSubmitted ? <TextFormat type="date" value={feedback.dateSubmitted} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{feedback.parent ? <Link to={`/parent/${feedback.parent.id}`}>{feedback.parent.id}</Link> : ''}</td>
                  <td>
                    {feedback.vaccination ? <Link to={`/vaccination/${feedback.vaccination.id}`}>{feedback.vaccination.id}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/feedback/${feedback.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/feedback/${feedback.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/feedback/${feedback.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="smartVaxApp.feedback.home.notFound">No Feedbacks found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={feedbackList && feedbackList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default Feedback;
