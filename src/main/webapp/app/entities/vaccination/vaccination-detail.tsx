import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './vaccination.reducer';

export const VaccinationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const vaccinationEntity = useAppSelector(state => state.vaccination.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="vaccinationDetailsHeading">
          <Translate contentKey="smartVaxApp.vaccination.detail.title">Vaccination</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{vaccinationEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="smartVaxApp.vaccination.name">Name</Translate>
            </span>
          </dt>
          <dd>{vaccinationEntity.name}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="smartVaxApp.vaccination.type">Type</Translate>
            </span>
          </dt>
          <dd>{vaccinationEntity.type}</dd>
          <dt>
            <span id="dateGiven">
              <Translate contentKey="smartVaxApp.vaccination.dateGiven">Date Given</Translate>
            </span>
          </dt>
          <dd>
            {vaccinationEntity.dateGiven ? (
              <TextFormat value={vaccinationEntity.dateGiven} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="sideEffects">
              <Translate contentKey="smartVaxApp.vaccination.sideEffects">Side Effects</Translate>
            </span>
          </dt>
          <dd>{vaccinationEntity.sideEffects}</dd>
          <dt>
            <span id="targetAge">
              <Translate contentKey="smartVaxApp.vaccination.targetAge">Target Age</Translate>
            </span>
          </dt>
          <dd>{vaccinationEntity.targetAge}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="smartVaxApp.vaccination.status">Status</Translate>
            </span>
          </dt>
          <dd>{vaccinationEntity.status}</dd>
          <dt>
            <span id="treatment">
              <Translate contentKey="smartVaxApp.vaccination.treatment">Treatment</Translate>
            </span>
          </dt>
          <dd>{vaccinationEntity.treatment}</dd>
        </dl>
        <Button tag={Link} to="/vaccination" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/vaccination/${vaccinationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default VaccinationDetail;
