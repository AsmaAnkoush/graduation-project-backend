import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './child.reducer';

export const ChildDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const childEntity = useAppSelector(state => state.child.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="childDetailsHeading">
          <Translate contentKey="smartVaxApp.child.detail.title">Child</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{childEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="smartVaxApp.child.name">Name</Translate>
            </span>
          </dt>
          <dd>{childEntity.name}</dd>
          <dt>
            <span id="dob">
              <Translate contentKey="smartVaxApp.child.dob">Dob</Translate>
            </span>
          </dt>
          <dd>{childEntity.dob ? <TextFormat value={childEntity.dob} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="weight">
              <Translate contentKey="smartVaxApp.child.weight">Weight</Translate>
            </span>
          </dt>
          <dd>{childEntity.weight}</dd>
          <dt>
            <span id="height">
              <Translate contentKey="smartVaxApp.child.height">Height</Translate>
            </span>
          </dt>
          <dd>{childEntity.height}</dd>
          <dt>
            <Translate contentKey="smartVaxApp.child.healthRecord">Health Record</Translate>
          </dt>
          <dd>{childEntity.healthRecord ? childEntity.healthRecord.id : ''}</dd>
          <dt>
            <Translate contentKey="smartVaxApp.child.parent">Parent</Translate>
          </dt>
          <dd>{childEntity.parent ? childEntity.parent.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/child" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/child/${childEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ChildDetail;
