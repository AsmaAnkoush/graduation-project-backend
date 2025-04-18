import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './health-worker.reducer';

export const HealthWorkerDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const healthWorkerEntity = useAppSelector(state => state.healthWorker.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="healthWorkerDetailsHeading">
          <Translate contentKey="smartVaxApp.healthWorker.detail.title">HealthWorker</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{healthWorkerEntity.id}</dd>
          <dt>
            <span id="username">
              <Translate contentKey="smartVaxApp.healthWorker.username">Username</Translate>
            </span>
          </dt>
          <dd>{healthWorkerEntity.username}</dd>
          <dt>
            <span id="password">
              <Translate contentKey="smartVaxApp.healthWorker.password">Password</Translate>
            </span>
          </dt>
          <dd>{healthWorkerEntity.password}</dd>
          <dt>
            <span id="phone">
              <Translate contentKey="smartVaxApp.healthWorker.phone">Phone</Translate>
            </span>
          </dt>
          <dd>{healthWorkerEntity.phone}</dd>
          <dt>
            <span id="age">
              <Translate contentKey="smartVaxApp.healthWorker.age">Age</Translate>
            </span>
          </dt>
          <dd>{healthWorkerEntity.age}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="smartVaxApp.healthWorker.name">Name</Translate>
            </span>
          </dt>
          <dd>{healthWorkerEntity.name}</dd>
          <dt>
            <span id="gender">
              <Translate contentKey="smartVaxApp.healthWorker.gender">Gender</Translate>
            </span>
          </dt>
          <dd>{healthWorkerEntity.gender}</dd>
          <dt>
            <span id="location">
              <Translate contentKey="smartVaxApp.healthWorker.location">Location</Translate>
            </span>
          </dt>
          <dd>{healthWorkerEntity.location}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="smartVaxApp.healthWorker.email">Email</Translate>
            </span>
          </dt>
          <dd>{healthWorkerEntity.email}</dd>
          <dt>
            <span id="role">
              <Translate contentKey="smartVaxApp.healthWorker.role">Role</Translate>
            </span>
          </dt>
          <dd>{healthWorkerEntity.role}</dd>
        </dl>
        <Button tag={Link} to="/health-worker" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/health-worker/${healthWorkerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default HealthWorkerDetail;
