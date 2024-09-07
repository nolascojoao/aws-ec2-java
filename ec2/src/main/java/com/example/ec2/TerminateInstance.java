package com.example.ec2;

import java.util.List;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.Ec2Exception;
import software.amazon.awssdk.services.ec2.model.InstanceStateChange;
import software.amazon.awssdk.services.ec2.model.TerminateInstancesRequest;
import software.amazon.awssdk.services.ec2.model.TerminateInstancesResponse;

public class TerminateInstance {

	public static void main(String[] args) {
		final String usage = 
				"Usage:\n\n"+
				"	<instanceId>"+
				"Where:\n\n"+
					"instanceId - An instance id value that you can obtain from the AWS Console";
		
		if (args.length != 1) {
			System.out.println(usage);
			System.exit(1);
		}
		
		String instanceId = args[0];
		Region region = Region.US_EAST_1;
		Ec2Client ec2 = Ec2Client.builder()
				.region(region)
				.build();
		terminateEC2(ec2, instanceId);
		ec2.close();
	}
	
	public static void terminateEC2(Ec2Client ec2, String instanceId) {
		try {
			TerminateInstancesRequest ti = TerminateInstancesRequest.builder()
					.instanceIds(instanceId)
					.build();
			TerminateInstancesResponse response = ec2.terminateInstances(ti);
			List<InstanceStateChange> list = response.terminatingInstances();
			for (InstanceStateChange sc : list) {
				System.out.println("The ID of the terminated instance is " + sc.instanceId());
			}
		} catch (Ec2Exception e) {
			System.err.println(e.awsErrorDetails().errorMessage());
			System.exit(1);
		}
	}
}
