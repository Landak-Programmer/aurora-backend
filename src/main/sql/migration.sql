CREATE TABLE `sms_inbox` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `message` varchar(255) NOT NULL,
  `hash` varchar(255) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT current_timestamp(),
  `date_created` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `sms_inbox`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `hash` (`hash`),
  ADD KEY `user_id` (`user_id`);

ALTER TABLE `sms_inbox`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

CREATE TABLE `transaction` (
  `id` bigint(20) NOT NULL,
  `wallet_id` bigint(20) NOT NULL,
  `reference` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `after_amount` decimal(10,2) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT current_timestamp(),
  `date_created` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `transaction`
  ADD PRIMARY KEY (`id`),
  ADD KEY `wallet_id` (`wallet_id`);

ALTER TABLE `transaction`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

CREATE TABLE `contact` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `phone_number` varchar(255) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT current_timestamp(),
  `date_created` datetime NOT NULL DEFAULT current_timestamp(),
   PRIMARY KEY (`id`),
   KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `voice_command` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `command` varchar(255) NOT NULL,
  `response_voice` varchar(255) NOT NULL,
  `response_action` varchar(255) NOT NULL,
  `response_type` varchar(255) NOT NULL,
  `precede_command_id` bigint(20),
  `param_tag` varchar(255),
  `is_chain` bit(1) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT current_timestamp(),
  `date_created` datetime NOT NULL DEFAULT current_timestamp(),
   PRIMARY KEY (`id`),
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
