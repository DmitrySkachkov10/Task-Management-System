--
-- PostgreSQL database dump
--

-- Dumped from database version 15.4
-- Dumped by pg_dump version 15.4

-- Started on 2024-08-09 17:08:31 +03

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 7 (class 2615 OID 88151)
-- Name: task_service; Type: SCHEMA; Schema: -; Owner: dmitry
--

CREATE SCHEMA task_service;


ALTER SCHEMA task_service OWNER TO dmitry;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 217 (class 1259 OID 88473)
-- Name: comment; Type: TABLE; Schema: task_service; Owner: dmitry
--

CREATE TABLE task_service.comment (
    commentator_uuid uuid,
    task_uuid uuid,
    uuid uuid NOT NULL,
    text character varying(255)
);


ALTER TABLE task_service.comment OWNER TO dmitry;

--
-- TOC entry 218 (class 1259 OID 88478)
-- Name: task; Type: TABLE; Schema: task_service; Owner: dmitry
--

CREATE TABLE task_service.task (
    version bigint NOT NULL,
    author_uuid uuid,
    uuid uuid NOT NULL,
    description character varying(255),
    header character varying(255),
    priority character varying(255),
    status character varying(255),
    CONSTRAINT task_priority_check CHECK (((priority)::text = ANY ((ARRAY['HIGH'::character varying, 'MEDIUM'::character varying, 'LOW'::character varying])::text[]))),
    CONSTRAINT task_status_check CHECK (((status)::text = ANY ((ARRAY['IN_PROGRESS'::character varying, 'FINISHED'::character varying, 'CANCELED'::character varying, 'PENDING'::character varying])::text[])))
);


ALTER TABLE task_service.task OWNER TO dmitry;

--
-- TOC entry 219 (class 1259 OID 88487)
-- Name: task_performer; Type: TABLE; Schema: task_service; Owner: dmitry
--

CREATE TABLE task_service.task_performer (
    performer_uuid uuid NOT NULL,
    task_uuid uuid NOT NULL
);


ALTER TABLE task_service.task_performer OWNER TO dmitry;

--
-- TOC entry 3454 (class 2606 OID 88477)
-- Name: comment comment_pkey; Type: CONSTRAINT; Schema: task_service; Owner: dmitry
--

ALTER TABLE ONLY task_service.comment
    ADD CONSTRAINT comment_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3458 (class 2606 OID 88491)
-- Name: task_performer task_performer_pkey; Type: CONSTRAINT; Schema: task_service; Owner: dmitry
--

ALTER TABLE ONLY task_service.task_performer
    ADD CONSTRAINT task_performer_pkey PRIMARY KEY (performer_uuid, task_uuid);


--
-- TOC entry 3456 (class 2606 OID 88486)
-- Name: task task_pkey; Type: CONSTRAINT; Schema: task_service; Owner: dmitry
--

ALTER TABLE ONLY task_service.task
    ADD CONSTRAINT task_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3460 (class 2606 OID 88497)
-- Name: task_performer fkdotnawox760l2pkau2y0jm1ga; Type: FK CONSTRAINT; Schema: task_service; Owner: dmitry
--

ALTER TABLE ONLY task_service.task_performer
    ADD CONSTRAINT fkdotnawox760l2pkau2y0jm1ga FOREIGN KEY (task_uuid) REFERENCES task_service.task(uuid);


--
-- TOC entry 3459 (class 2606 OID 88492)
-- Name: comment fkek7o50nua63esew5jfhpag0ct; Type: FK CONSTRAINT; Schema: task_service; Owner: dmitry
--

ALTER TABLE ONLY task_service.comment
    ADD CONSTRAINT fkek7o50nua63esew5jfhpag0ct FOREIGN KEY (task_uuid) REFERENCES task_service.task(uuid);


-- Completed on 2024-08-09 17:08:32 +03

--
-- PostgreSQL database dump complete
--

